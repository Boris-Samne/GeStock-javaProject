package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;




public class Fournisseur {
    private final IntegerProperty id;
    private final StringProperty nom;
    private final StringProperty adresse;
    private final StringProperty telephone;
    private final StringProperty rib;
    

    @FXML
    private TextField nomTextField, adresseTextField, telephoneTextField, ribTextField;

    @FXML
    private TableView<Fournisseur> fournisseurTableView;

    @FXML
    private TableColumn<Fournisseur, Integer> idColumn;

    @FXML
    private TableColumn<Fournisseur, String> nomColumn;

    @FXML
    private TableColumn<Fournisseur, String> adresseColumn;

    @FXML
    private TableColumn<Fournisseur, String> telephoneColumn;

    @FXML
    private TableColumn<Fournisseur, String> ribColumn;

    @FXML
    private Button printButton;

    @FXML
    private TextField fsearchField;

    private static ObservableList<Fournisseur> fournisseurList = FXCollections.observableArrayList();

    private ObservableList<Fournisseur> filteredData = FXCollections.observableArrayList();

    public Fournisseur() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.adresse = new SimpleStringProperty();
        this.telephone = new SimpleStringProperty();
        this.rib = new SimpleStringProperty();
    }

    @Override
    public String toString(){
        return this.getNom();
    }

    public Fournisseur(int id, String nom, String adresse, String telephone, String rib) {
        this.id = new SimpleIntegerProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.adresse = new SimpleStringProperty(adresse);
        this.telephone = new SimpleStringProperty(telephone);
        this.rib = new SimpleStringProperty(rib);
    }

    // Getters and setters for id
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    // Getters and setters for nom
    public String getNom() {
        return this.nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    // Getters and setters for adresse
    public String getAdresse() {
        return adresse.get();
    }

    public StringProperty adresseProperty() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse.set(adresse);
    }

    // Getters and setters for telephone
    public String getTelephone() {
        return telephone.get();
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    // Getters and setters for rib
    public String getRib() {
        return rib.get();
    }

    public StringProperty ribProperty() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib.set(rib);
    }


    //les fonctions de gestions du Fournisseur
    @FXML
    private void initialize() {
        // Charger des données dans la TableView
        ObservableList<Fournisseur> fournisseurs = loadFournisseurs();
        idColumn.setCellValueFactory(new PropertyValueFactory<Fournisseur, Integer>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<Fournisseur, String>("nom"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<Fournisseur, String>("adresse"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<Fournisseur, String>("telephone"));
        ribColumn.setCellValueFactory(new PropertyValueFactory<Fournisseur, String>("rib"));

        // Ajouter une colonne personnalisée avec un bouton de modification
        TableColumn<Fournisseur, Void> editColumn = new TableColumn<>("Modifier");
        editColumn.setCellFactory(editFournisseurCellFactory());
        fournisseurTableView.getColumns().add(editColumn);

        //Supprimer les Informations.
        TableColumn<Fournisseur, Void> deleteColumn = new TableColumn<>("Supprimer");
        deleteColumn.setCellFactory(deleteFournisseurCellFactory());
        fournisseurTableView.getColumns().add(deleteColumn);



        // Appliquer les données non filtrées à la TableView
        fournisseurTableView.setItems(fournisseurs);
        // Ajouter un écouteur sur le champ de recherche pour filtrer les données
        fsearchField.textProperty().addListener((observable, oldValue, newValue) -> filterData(newValue));
}

private void filterData(String newValue) {
    filteredData.clear();
    if (newValue == null || newValue.isEmpty()) {
        filteredData.addAll(fournisseurList);
    } else {
        String lowerCaseFilter = newValue.toLowerCase();
        for (Fournisseur fournisseur : fournisseurList) {
            if (String.valueOf(fournisseur.getId()).toLowerCase().contains(lowerCaseFilter) ||
                fournisseur.getNom().toLowerCase().contains(lowerCaseFilter) ||
                fournisseur.getAdresse().toLowerCase().contains(lowerCaseFilter) ||
                fournisseur.getTelephone().toLowerCase().contains(lowerCaseFilter) ||
                fournisseur.getRib().toLowerCase().contains(lowerCaseFilter)) {
                filteredData.add(fournisseur);
            }
        }
    }
    fournisseurTableView.setItems(filteredData);
}


    public static ObservableList<Fournisseur> loadFournisseurs() {
        String query = "SELECT * FROM fournisseurs";
        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("Id");
                String nom = rs.getString("Nom");
                String adresse = rs.getString("Adresse");
                String telephone = rs.getString("Telephone");
                String rib = rs.getString("RIB");

                Fournisseur fournisseur = new Fournisseur(id,nom,adresse,telephone,rib);
                fournisseurList.add(fournisseur);
            }
        } catch (SQLException e) {  
            System.out.println("Error: " + e.getMessage());
        }
        return fournisseurList;
    }

    @FXML
    void enregistrerFournisseur(ActionEvent event) {
        String nom = nomTextField.getText();
        String adresse = adresseTextField.getText();
        String telephone = telephoneTextField.getText();
        String rib = ribTextField.getText();

        String query = "INSERT INTO fournisseurs (Nom, Adresse, Telephone, RIB) VALUES (?, ?, ?, ?)";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nom);
            statement.setString(2, adresse);
            statement.setString(3, telephone);
            statement.setString(4, rib);
            statement.executeUpdate();
            System.out.println("Données insérées avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion des données : " + e.getMessage());
        }

        // Actualiser la TableView après l'insertion
        fournisseurTableView.getItems().clear();
        loadFournisseurs();
    }

    @FXML
    void annuler(ActionEvent event) {
        // Effacer les champs de texte
        nomTextField.clear();
        adresseTextField.clear();
        telephoneTextField.clear();
        ribTextField.clear();
    }
    @FXML
    void nouveauFournisseur(ActionEvent event) {
        // Créer une boîte de dialogue pour la saisie d'un nouveau fournisseur
        Dialog<Fournisseur> dialog = new Dialog<>();
        dialog.setTitle("Nouveau fournisseur");
        dialog.setHeaderText("Saisir les informations du nouveau fournisseur");
    
        // Définir le bouton Enregistrer
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
    
        // Créer les champs de saisie pour les nouvelles informations du fournisseur
        TextField nomField = new TextField();
        TextField adresseField = new TextField();
        TextField telephoneField = new TextField();
        TextField ribField = new TextField();
    
        // Ajouter les champs de saisie à la boîte de dialogue
        GridPane grid = new GridPane();
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Adresse:"), 0, 1);
        grid.add(adresseField, 1, 1);
        grid.add(new Label("Téléphone:"), 0, 2);
        grid.add(telephoneField, 1, 2);
        grid.add(new Label("RIB:"), 0, 3);
        grid.add(ribField, 1, 3);
    
        dialog.getDialogPane().setContent(grid);
    
        // Demander à la boîte de dialogue de se concentrer sur le champ de nom par défaut
        Platform.runLater(() -> nomField.requestFocus());
    
        // Convertir le résultat de la boîte de dialogue en objet Fournisseur
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Créer un nouveau fournisseur avec les nouvelles informations
                Fournisseur nouveauFournisseur = new Fournisseur();
                nouveauFournisseur.setNom(nomField.getText());
                nouveauFournisseur.setAdresse(adresseField.getText());
                nouveauFournisseur.setTelephone(telephoneField.getText());
                nouveauFournisseur.setRib(ribField.getText());
                return nouveauFournisseur;
            }
            return null;
        });
    
        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        Optional<Fournisseur> result = dialog.showAndWait();
    
        // Si l'utilisateur clique sur le bouton Enregistrer, enregistrer le nouveau fournisseur
        result.ifPresent(nouveauFournisseur -> {String query = "INSERT INTO fournisseurs (Nom, Adresse, Telephone, RIB) VALUES (?, ?, ?, ?)";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nouveauFournisseur.getNom());
            statement.setString(2, nouveauFournisseur.getAdresse());
            statement.setString(3, nouveauFournisseur.getTelephone());
            statement.setString(4, nouveauFournisseur.getRib());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                afficherAlerte("Nouveau fournisseur enregistré avec succès !");
                // Actualiser la TableView après l'insertion
                fournisseurList.clear();
                fournisseurList.addAll(loadFournisseurs());
            } else {
                afficherAlerte("Erreur lors de l'enregistrement du nouveau fournisseur !");
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur lors de l'enregistrement du nouveau fournisseur : " + e.getMessage());
        }
        });
    }
    private void afficherAlerte(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    void printFournisseurs(ActionEvent event) {
    try  {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            // Titre du document
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Liste des Fournisseurs");
            contentStream.endText();

            // Contenu des ventes
            int y = 650; // Position verticale initiale
            for (Fournisseur fournisseur : fournisseurList) {
                String fournisseurText = fournisseur.getId()+ " - " + fournisseur.getNom() + " - " + fournisseur.getTelephone() + " - " +
                fournisseur.getAdresse()+  " - " + fournisseur.getRib();
                contentStream.beginText();
                contentStream.newLineAtOffset(100, y);
                contentStream.showText(fournisseurText);
                contentStream.endText();
                y -= 20; // Déplacement vers le haut pour la prochaine vente
            }
        }
        // Obtention de la date et de l'heure actuelles
        LocalDateTime dateEtHeureActuelles = LocalDateTime.now();

        // Création d'un formateur de date et d'heure personnalisé
        DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");

        // Formatage et affichage de la date et de l'heure actuelles
        String dateEtHeureFormatees = dateEtHeureActuelles.format(formateur);


        // Enregistrement du document dans le dossier de téléchargement par défaut
        String home = System.getProperty("user.home");
        File file = new File(home + "/Downloads/ListeFournisseurs"+dateEtHeureFormatees+".pdf");
        document.save(file);

        System.out.println("Le fichier PDF a été généré avec succès.");
        afficherAlerte("La liste des Fournisseurs a été générée avec succès dans votre dossier de telechargement courant.");
    } catch (IOException e) {
        System.err.println("Erreur lors de la génération du fichier PDF : " + e.getMessage());
    }
    }

    // Méthode pour créer une cellule personnalisée avec un bouton de suppression
    public Callback<TableColumn<Fournisseur, Void>, TableCell<Fournisseur, Void>> deleteFournisseurCellFactory() {
            return new Callback<TableColumn<Fournisseur, Void>, TableCell<Fournisseur, Void>>() {
                @Override
                public TableCell<Fournisseur, Void> call(final TableColumn<Fournisseur, Void> param) {
                    return new TableCell<Fournisseur, Void>() {
                        private final Button deleteButton = new Button("Supprimer");
    
                        {
                            deleteButton.setOnAction(event -> {
                                Fournisseur fournisseur = getTableView().getItems().get(getIndex());
    
                                // Créer une boîte de dialogue de confirmation
                                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                                confirmationDialog.setTitle("Confirmation de suppression");
                                confirmationDialog.setHeaderText("Supprimer le fournisseur ?");
                                confirmationDialog.setContentText("Êtes-vous sûr de vouloir supprimer le fournisseur : " + fournisseur.getNom() + " ?");
    
                                // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                                Optional<ButtonType> result = confirmationDialog.showAndWait();
    
                                // Si l'utilisateur clique sur le bouton OK, supprimer le fournisseur
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    String query = "DELETE FROM fournisseurs WHERE Id = ?";
                                    try (Connection cn = DB.getConnection();
                                         PreparedStatement stmt = cn.prepareStatement(query)) {
                                        stmt.setInt(1, fournisseur.getId());
                                        int rowsAffected = stmt.executeUpdate();
                                        if (rowsAffected > 0) {
                                            System.out.println("Suppression réussie du fournisseur : " + fournisseur.getNom());
                                            // Actualiser la TableView après la suppression
                                            getTableView().getItems().remove(fournisseur);
                                        } else {
                                            System.out.println("Aucun fournisseur supprimé.");
                                        }
                                    } catch (SQLException e) {
                                        System.out.println("Erreur de suppression du fournisseur : " + fournisseur.getNom());
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
    
                        @Override
                        protected void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(deleteButton);
                            }
                        }
                    };
                }
            };
        }

    public Callback<TableColumn<Fournisseur, Void>, TableCell<Fournisseur, Void>> editFournisseurCellFactory() {
        return new Callback<TableColumn<Fournisseur, Void>, TableCell<Fournisseur, Void>>() {
            @Override
            public TableCell<Fournisseur, Void> call(final TableColumn<Fournisseur, Void> param) {
                return new TableCell<Fournisseur, Void>() {
                    private final Button editButton = new Button("Modifier");

                    {
                        editButton.setOnAction(event -> {
                            Fournisseur fournisseur = getTableView().getItems().get(getIndex());

                            // Créer une boîte de dialogue pour la modification du fournisseur
                            Dialog<Fournisseur> dialog = new Dialog<>();
                            dialog.setTitle("Modifier le fournisseur");
                            dialog.setHeaderText("Modifier les informations du fournisseur : " + fournisseur.getNom());

                            // Définir le bouton Enregistrer
                            ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
                            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                            // Créer les champs de saisie pour les nouvelles informations du fournisseur
                            TextField nomField = new TextField(fournisseur.getNom());
                            TextField adresseField = new TextField(fournisseur.getAdresse());
                            TextField telephoneField = new TextField(fournisseur.getTelephone());
                            TextField ribField = new TextField(fournisseur.getRib());

                            // Ajouter les champs de saisie à la boîte de dialogue
                            GridPane grid = new GridPane();
                            grid.add(new Label("Nom:"), 0, 0);
                            grid.add(nomField, 1, 0);
                            grid.add(new Label("Adresse:"), 0, 1);
                            grid.add(adresseField, 1, 1);
                            grid.add(new Label("Téléphone:"), 0, 2);
                            grid.add(telephoneField, 1, 2);
                            grid.add(new Label("RIB:"), 0, 3);
                            grid.add(ribField, 1, 3);

                            dialog.getDialogPane().setContent(grid);

                            // Demander à la boîte de dialogue de se concentrer sur le champ de nom par défaut
                            Platform.runLater(() -> nomField.requestFocus());

                            // Convertir le résultat de la boîte de dialogue en objet Fournisseur
                            dialog.setResultConverter(dialogButton -> {
                                if (dialogButton == saveButtonType) {
                                    // Créer un nouveau fournisseur avec les nouvelles informations
                                    Fournisseur editedFournisseur = new Fournisseur();
                                    editedFournisseur.setNom(nomField.getText());
                                    editedFournisseur.setAdresse(adresseField.getText());
                                    editedFournisseur.setTelephone(telephoneField.getText());
                                    editedFournisseur.setRib(ribField.getText());
                                    return editedFournisseur;
                                }
                                return null;
                            });

                            // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                            Optional<Fournisseur> result = dialog.showAndWait();

                            // Si l'utilisateur clique sur le bouton Enregistrer, enregistrer les modifications
                            result.ifPresent(editedFournisseur -> {
                                // Enregistrer les modifications dans la base de données
                                String query = "UPDATE fournisseurs SET Nom = ?, Adresse = ?, Telephone = ?, RIB = ? WHERE Id = ?";
                                try (Connection cn = DB.getConnection();
                                        PreparedStatement stmt = cn.prepareStatement(query)) {
                                    stmt.setString(1, editedFournisseur.getNom());
                                    stmt.setString(2, editedFournisseur.getAdresse());
                                    stmt.setString(3, editedFournisseur.getTelephone());
                                    stmt.setString(4, editedFournisseur.getRib());
                                    stmt.setInt(5, fournisseur.getId());
                                    int rowsAffected = stmt.executeUpdate();
                                    if (rowsAffected > 0) {
                                        System.out.println("Modification réussie du fournisseur : " + fournisseur.getNom());
                                        // Actualiser la TableView après la modification
                                        fournisseur.setNom(editedFournisseur.getNom());
                                        fournisseur.setAdresse(editedFournisseur.getAdresse());
                                        fournisseur.setTelephone(editedFournisseur.getTelephone());
                                        fournisseur.setRib(editedFournisseur.getRib());
                                        getTableView().refresh();
                                    } else {
                                        System.out.println("Aucune modification apportée.");
                                    }
                                } catch (SQLException e) {
                                    System.out.println("Erreur de modification du fournisseur : " + fournisseur.getNom());
                                    e.printStackTrace();
                                }
                            });
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(editButton);
                        }
                    }
                };
            }
        };
    }
}
