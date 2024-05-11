package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Vente {
    private final IntegerProperty id;
    private final IntegerProperty idArticle;
    private final IntegerProperty idClient;
    private final IntegerProperty quantite;
    private final StringProperty date;
    private final StringProperty nomClient;
    private final StringProperty libelleArticle;
    private final DoubleProperty prixUnitaire;
    private final DoubleProperty tva;
    private final DoubleProperty montantVente;

    @FXML
    private TableView<Vente> venteTableView;

    @FXML
    private TableColumn<Vente, Integer> idColumn;

    @FXML
    private TableColumn<Vente, Integer> idArticleColumn;

    @FXML
    private TableColumn<Vente, Integer> idClientColumn;

    @FXML
    private TableColumn<Vente, Integer> quantiteColumn;

    @FXML
    private TableColumn<Vente, String> nomClientColumn;

    @FXML
    private TableColumn<Vente, String> libelleArticleColumn;

    @FXML
    private TableColumn<Vente, Double> montantVenteColumn;

    @FXML
    private TableColumn<Vente, String> dateColumn;
    
    // Map pour stocker les états des champs (true si un champ a déjà déclenché l'ajout d'un autre champ, false sinon)
    private Map<TextField, Boolean> champEtatMap = new HashMap<>();

    @FXML
    private VBox vbox; // Le conteneur pour les champs de saisie dynamiques

    ObservableList<Vente> ventes;


    @FXML
    private TextField vsearchField;

    private ObservableList<Vente> venteList = FXCollections.observableArrayList();
    private ObservableList<Vente> filteredData = FXCollections.observableArrayList();

    public Vente() {
        this.id = new SimpleIntegerProperty();
        this.idArticle = new SimpleIntegerProperty();
        this.idClient = new SimpleIntegerProperty();
        this.quantite = new SimpleIntegerProperty();
        this.date = new SimpleStringProperty();
        this.nomClient = new SimpleStringProperty();
        this.libelleArticle = new SimpleStringProperty();
        this.prixUnitaire = new SimpleDoubleProperty();
        this.tva = new SimpleDoubleProperty();
        this.montantVente = new SimpleDoubleProperty();
    }

    @Override
    public String toString() {
        return String.valueOf(this.getId());
    }

    public Vente(int id, int idArticle, int idClient, int quantite, String date) {
        this.id = new SimpleIntegerProperty(id);
        this.idArticle = new SimpleIntegerProperty(idArticle);
        this.idClient = new SimpleIntegerProperty(idClient);
        this.quantite = new SimpleIntegerProperty(quantite);
        this.date = new SimpleStringProperty(date);
        this.nomClient = new SimpleStringProperty();
        this.libelleArticle = new SimpleStringProperty();
        this.prixUnitaire = new SimpleDoubleProperty();
        this.tva = new SimpleDoubleProperty();
        this.montantVente = new SimpleDoubleProperty();
    }

    public Vente(int id, String nomClient, String libelleArticle, int quantite, double prixUnitaire, double tva, Date date) {
        this.id = new SimpleIntegerProperty(id);
        this.nomClient = new SimpleStringProperty(nomClient);
        this.libelleArticle = new SimpleStringProperty(libelleArticle);
        this.quantite = new SimpleIntegerProperty(quantite);
        this.prixUnitaire = new SimpleDoubleProperty(prixUnitaire);
        this.tva = new SimpleDoubleProperty(tva);
        this.montantVente = new SimpleDoubleProperty(calcularMontantVente(quantite, prixUnitaire, tva));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Format de date et heure
        this.date = new SimpleStringProperty(dateFormat.format(date)); // Convertir la date en chaîne de caractères
        this.idArticle = new SimpleIntegerProperty();
        this.idClient = new SimpleIntegerProperty();
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

    // Getters and setters for idArticle
    public int getIdArticle() {
        return idArticle.get();
    }

    public IntegerProperty idArticleProperty() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle.set(idArticle);
    }

    // Getters and setters for idClient
    public int getIdClient() {
        return idClient.get();
    }

    public IntegerProperty idClientProperty() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient.set(idClient);
    }

    // Getters and setters for nomClient
    public String getNomClient() {
        return nomClient.get();
    }

    public StringProperty nomClientProperty() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient.set(nomClient);
    }

        // Getters and setters for libelleArticle
        public String getLibelleArticle() {
            return libelleArticle.get();
        }
    
        public StringProperty libelleArticleProperty() {
            return libelleArticle;
        }
    
        public void setLibelleArticle(String libelleArticle) {
            this.libelleArticle.set(libelleArticle);
        }
        // Getters and setters for prixUnitaire
        public Double getPrixUnitaire() {
            return prixUnitaire.get();
        }
    
        public DoubleProperty prixUnitaireProperty() {
            return prixUnitaire;
        }
    
        public void setPrixUnitaire(Double tva) {
            this.prixUnitaire.set(tva);
        }
        // Getters and setters for tva
        public Double getTva() {
            return tva.get();
        }
    
        public DoubleProperty tvaProperty() {
            return tva;
        }
    
        public void setTva(Double tva) {
            this.tva.set(tva);
        }       
        // Getters and setters for montantVente
        public Double getMontantVente() {
            return montantVente.get();
        }
    
        public DoubleProperty montantVenteProperty() {
            return montantVente;
        }
    
        public void setMontantVente(Double montantVente) {
            this.montantVente.set(montantVente);
        }
    // Getters and setters for date
    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

        // Getters and setters for quantite
    public int getQuantite() {
        return quantite.get();
    }

    public IntegerProperty quantiteProperty() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite.set(quantite);
    }


    // Méthode pour calculer le montant de la vente
    private double calcularMontantVente(int quantite, double prixUnitaire, double tva) {
        return (quantite * prixUnitaire) + (quantite * prixUnitaire * tva / 100);
    }

    // Méthode pour charger les ventes depuis la base de données
    public static ObservableList<Vente> loadVentes() {
        ObservableList<Vente> ventes = FXCollections.observableArrayList();

        String query = "SELECT v.Id, c.Nom AS NomClient, a.Libelle AS LibelleArticle, v.Quantite, a.Prix_vente AS PrixUnitaire, v.date as date " +
        "FROM ventes v " +
        "INNER JOIN clients c ON v.IdClient = c.Id " +
        "INNER JOIN articles a ON v.IdArticle = a.Id";

        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Id");
                String nomClient = rs.getString("NomClient");
                String libelleArticle = rs.getString("LibelleArticle");
                int quantite = rs.getInt("Quantite");
                double prixUnitaire = rs.getDouble("PrixUnitaire");
                Date date= rs.getDate("date");
                double tva = 10;
                // rs.getDouble("TVA");

                Vente vente = new Vente(id, nomClient, libelleArticle, quantite, prixUnitaire, tva, date);
                ventes.add(vente);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return ventes;
    }
    @FXML
    private void initialize() {
        ajouterComboBoxClients();
        ajouterChamp();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomClientColumn.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        libelleArticleColumn.setCellValueFactory(new PropertyValueFactory<>("libelleArticle"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        montantVenteColumn.setCellValueFactory(new PropertyValueFactory<>("montantVente"));

        // ventes = loadVentes();
        // venteList.addAll(ventes); // ajout des ventes à la liste
        // venteTableView.setItems(venteList);
        // vsearchField.textProperty().addListener((observable, oldValue, newValue) -> filterData(newValue));

        // Ajouter une colonne personnalisée avec un bouton de modification
        TableColumn<Vente, Void> editColumn = new TableColumn<>("Modifier");
        editColumn.setCellFactory(editVenteCellFactory());
        venteTableView.getColumns().add(editColumn);

        // Ajouter une colonne personnalisée avec un bouton de suppression
        TableColumn<Vente, Void> deleteColumn = new TableColumn<>("Supprimer");
        deleteColumn.setCellFactory(deleteVenteCellFactory());
        venteTableView.getColumns().add(deleteColumn);
        
        ActualiserVentes();
    }

    private void filterData(String newValue) {
        filteredData.clear();
        if (newValue == null || newValue.isEmpty()) {
            filteredData.addAll(venteList);
        } else {
            String lowerCaseFilter = newValue.toLowerCase();
            for (Vente vente : venteList) {
                if (String.valueOf(vente.getId()).toLowerCase().contains(lowerCaseFilter) ||
                        vente.getNomClient().toLowerCase().contains(lowerCaseFilter) ||
                        vente.getLibelleArticle().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(vente.getQuantite()).toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(vente.getMontantVente()).toLowerCase().contains(lowerCaseFilter)) {
                    filteredData.add(vente);
                }
            }
        }
        venteTableView.setItems(filteredData);
    }
    
    
// Méthode pour ajouter un nouveau champ de saisie
private void ajouterChamp() {
    // Créer un ComboBox pour les articles
    ComboBox<Article> articleComboBox = new ComboBox<>();
    // Récupérer les articles depuis la base de données et les ajouter au ComboBox
    chargerArticles(articleComboBox);

    TextField quantiteField = new TextField(); // Créer un champ de saisie pour la quantité
    quantiteField.setPromptText("Quantité");
    quantiteField.setId("quantiteField");

    // Ajouter un écouteur de modification à ce champ de saisie
    quantiteField.textProperty().addListener((observable, oldValue, newValue) -> {
        if (!newValue.isEmpty() && !champEtatMap.getOrDefault(quantiteField, false)) {
            champEtatMap.put(quantiteField, true); // Marquer le champ comme ayant déclenché l'ajout d'un autre champ
            ajouterChamp(); // Ajouter un nouveau champ
        }
    });

    HBox hbox = new HBox(articleComboBox, quantiteField); // Créer une ligne pour les champs
    hbox.setSpacing(10);
    vbox.getChildren().add(hbox); // Ajouter la ligne à la VBox
}

// Méthode pour ajouter le ComboBox des clients une seule fois
private void ajouterComboBoxClients() {
    ComboBox<String> clientComboBox = new ComboBox<>();
    chargerClients(clientComboBox); // Charger les noms des clients dans le ComboBox
    vbox.getChildren().add(clientComboBox); // Ajouter le ComboBox des clients à la VBox
}

// Méthode pour charger les clients depuis la base de données et les ajouter au ComboBox
private void chargerClients(ComboBox<String> clientComboBox) {
    // Sélectionner les noms des clients depuis la base de données
    String query = "SELECT Nom FROM clients";
    try (Connection connection = DB.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
            String nomClient = resultSet.getString("Nom");
            clientComboBox.getItems().add(nomClient);
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors du chargement des clients : " + e.getMessage());
    }
}

// Méthode pour charger les articles depuis la base de données et les ajouter au ComboBox
private void chargerArticles(ComboBox<Article> articleComboBox) {
    // Sélectionner les articles depuis la base de données
    String query = "SELECT * FROM articles";
    try (Connection connection = DB.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
            int id = resultSet.getInt("Id");
            String reference = resultSet.getString("Reference");
            String libelle = resultSet.getString("Libelle");
            Article article = new Article(id, reference, libelle);
            articleComboBox.getItems().add(article);
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors du chargement des articles : " + e.getMessage());
    }
}


    // Méthode pour enregistrer les ventes
// Méthode pour enregistrer les ventes
@FXML
void enregistrerVentes() {
    // Récupérer le ComboBox des clients
    ComboBox<String> clientComboBox = (ComboBox<String>) vbox.getChildren().get(0);
    String nomClientSelectionne = clientComboBox.getValue();
    int idClient = getIdClientFromNom(nomClientSelectionne); // Récupérer l'ID du client sélectionné

    for (int i = 1; i < vbox.getChildren().size(); i++) { // Commencer à l'index 1 car l'index 0 contient le ComboBox des clients
        HBox hbox = (HBox) vbox.getChildren().get(i);
        Node node = hbox.getChildren().get(0);
        if (node instanceof ComboBox) {
            ComboBox<Article> articleComboBox = (ComboBox<Article>) node;
            TextField quantiteField = (TextField) hbox.getChildren().get(1);

            // Récupérer les valeurs sélectionnées dans les champs
            Article article = articleComboBox.getValue();
            String quantiteText = quantiteField.getText().trim(); // Supprimer les espaces vides
            if (!quantiteText.isEmpty()) { // Vérifier si le champ de quantité n'est pas vide
                int quantite = Integer.parseInt(quantiteText);

                // Enregistrer la vente
                if (article != null && idClient !=-1) {
                    enregistrerVente(article.getId(), idClient, quantite); // Utiliser l'ID du client sélectionné
                    System.out.println(article.getId()+" "+ idClient+" "+quantite);
                }
                else if(article != null && idClient==-1){
                    enregistrerVente(article.getId(), quantite);
                    System.out.println(article.getId()+" "+ idClient+" "+quantite);

                }
            }
        }
        if(i==vbox.getChildren().size()-1){
            afficherAlerte("Ventes insérées avec Succes");
        }
    }
    ActualiserVentes();
}

// Méthode pour récupérer l'ID du client à partir de son nom
private int getIdClientFromNom(String nomClient) {
    int idClient = -1; // Valeur par défaut si aucun client n'est sélectionné
    String query = "SELECT Id FROM clients WHERE Nom = ?";
    try (Connection connection = DB.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, nomClient);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            idClient = resultSet.getInt("Id");
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération de l'ID du client : " + e.getMessage());
    }
    return idClient;
}


    // Méthode pour enregistrer une vente dans la base de données
    private void enregistrerVente(int idArticle, int quantite) {
        // Utiliser les valeurs par défaut pour les champs non remplis
        int idClient = 7; // Valeur par défaut pour l'ID du client
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Enregistrer la vente dans la base de données
        String query = "INSERT INTO ventes (IdArticle, IdClient, Quantite, Date) VALUES (?, ?, ?, ?)";
        String updateQuantiteQuery = "UPDATE articles SET stock = stock - ? WHERE Id = ?";
        try (Connection connection = DB.getConnection();
            PreparedStatement updateQuantiteStatement = connection.prepareStatement(updateQuantiteQuery);
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idArticle);
            statement.setInt(2, idClient);
            statement.setInt(3, quantite);
            statement.setString(4, date);
            statement.executeUpdate();

            updateQuantiteStatement.setInt(1, quantite);
            updateQuantiteStatement.setInt(2, idArticle);
            updateQuantiteStatement.executeUpdate();

            System.out.println("Vente enregistrée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement de la vente : " + e.getMessage());
        }
    }

    private void ActualiserVentes(){
        ventes = loadVentes();
        venteList.addAll(ventes); // ajout des ventes à la liste
        venteTableView.setItems(venteList);
        vsearchField.textProperty().addListener((observable, oldValue, newValue) -> filterData(newValue));
    }

    // Méthode pour enregistrer une vente dans la base de données
    private void enregistrerVente(int idArticle, int idClient, int quantite) {
        // Utiliser les valeurs par défaut pour les champs non remplis
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    
        // Vérifier si l'article correspondant à idArticle existe
        if (articleExists(idArticle)) {
            // Enregistrer la vente dans la base de données
            String venteQuery = "INSERT INTO ventes (IdArticle, IdClient, Quantite, Date) VALUES (?, ?, ?, ?)";
            String updateQuantiteQuery = "UPDATE articles SET stock = stock - ? WHERE Id = ?";
            try (Connection connection = DB.getConnection();
                 PreparedStatement venteStatement = connection.prepareStatement(venteQuery);
                 PreparedStatement updateQuantiteStatement = connection.prepareStatement(updateQuantiteQuery)) {
    
                venteStatement.setInt(1, idArticle);
                venteStatement.setInt(2, idClient);
                venteStatement.setInt(3, quantite);
                venteStatement.setString(4, date);
                venteStatement.executeUpdate();
    
                updateQuantiteStatement.setInt(1, quantite);
                updateQuantiteStatement.setInt(2, idArticle);
                updateQuantiteStatement.executeUpdate();
    
                System.out.println("Vente enregistrée avec succès !");
            } catch (SQLException e) {
                System.err.println("Erreur lors de l'enregistrement de la vente : " + e.getMessage());
            }
        } else {
            // Émettre une alerte si l'article n'existe pas
            afficherAlerte("L'article avec l'ID " + idArticle + " n'existe pas.");
        }
    }

    private boolean articleExists(int idArticle) {
        String query = "SELECT COUNT(*) AS count FROM articles WHERE Id = ?";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idArticle);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence de l'article : " + e.getMessage());
        }
        return false;
    }

@FXML
public void printVentes() {
    try  {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            // Titre du document
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Liste des Ventes");
            contentStream.endText();

            // Contenu des ventes
            int y = 650; // Position verticale initiale
            for (Vente vente : venteList) {
                String venteText = vente.getId() + " - " + vente.getNomClient() + " - " +
                                  vente.getLibelleArticle() + " - " + vente.getQuantite() +
                                  " - " + vente.getMontantVente()+
                                  " - " + vente.getDate();
                contentStream.beginText();
                contentStream.newLineAtOffset(100, y);
                contentStream.showText(venteText);
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
        File file = new File(home + "/Downloads/ListeVentes"+dateEtHeureFormatees+".pdf");
        document.save(file);

        System.out.println("Le fichier PDF a été généré avec succès.");
        afficherAlerte("La liste des ventes a été générée avec succès dans votre dossier de telechargement courant.");
    } catch (IOException e) {
        System.err.println("Erreur lors de la génération du fichier PDF : " + e.getMessage());
    }
}

private void afficherAlerte(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Info");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

    // Méthode pour modifier une vente
public Callback<TableColumn<Vente, Void>, TableCell<Vente, Void>> editVenteCellFactory() {
    return new Callback<TableColumn<Vente, Void>, TableCell<Vente, Void>>() {
        @Override
        public TableCell<Vente, Void> call(final TableColumn<Vente, Void> param) {
            return new TableCell<Vente, Void>() {
                private final Button editButton = new Button("Modifier");

                {
                    editButton.setOnAction(event -> {
                        Vente vente = getTableView().getItems().get(getIndex());

                        // Créer une boîte de dialogue pour la modification de la vente
                        Dialog<Vente> dialog = new Dialog<>();
                        dialog.setTitle("Modifier la vente");
                        dialog.setHeaderText("Modifier les informations de la vente");

                        // Définir le bouton Enregistrer
                        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                        // Créer les champs de saisie pour les nouvelles informations de la vente
                        TextField idArticleField = new TextField(String.valueOf(vente.getIdArticle()));
                        TextField idClientField = new TextField(String.valueOf(vente.getIdClient()));
                        TextField quantiteField = new TextField(String.valueOf(vente.getQuantite()));
                        TextField dateField = new TextField(vente.getDate());

                        // Ajouter les champs de saisie à la boîte de dialogue
                        GridPane grid = new GridPane();
                        grid.add(new Label("ID Article:"), 0, 0);
                        grid.add(idArticleField, 1, 0);
                        grid.add(new Label("ID Client:"), 0, 1);
                        grid.add(idClientField, 1, 1);
                        grid.add(new Label("Quantité:"), 0, 2);
                        grid.add(quantiteField, 1, 2);
                        grid.add(new Label("Date:"), 0, 3);
                        grid.add(dateField, 1, 3);

                        dialog.getDialogPane().setContent(grid);

                        // Demander à la boîte de dialogue de se concentrer sur le champ de ID Article par défaut
                        Platform.runLater(() -> idArticleField.requestFocus());

                        // Convertir le résultat de la boîte de dialogue en objet Vente
                        dialog.setResultConverter(dialogButton -> {
                            if (dialogButton == saveButtonType) {
                                // Créer un objet Vente modifié avec les nouvelles informations
                                Vente modifiedVente = new Vente();
                                modifiedVente.setIdArticle(Integer.parseInt(idArticleField.getText()));
                                modifiedVente.setIdClient(Integer.parseInt(idClientField.getText()));
                                modifiedVente.setQuantite(Integer.parseInt(quantiteField.getText()));
                                modifiedVente.setDate(dateField.getText());
                                return modifiedVente;
                            }
                            return null;
                        });

                        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                        Optional<Vente> result = dialog.showAndWait();

                        // Si l'utilisateur clique sur le bouton Enregistrer, enregistrer les modifications
                        result.ifPresent(modifiedVente -> {
                            // Mettre à jour la vente dans la base de données
                            String query = "UPDATE ventes SET IdArticle = ?, IdClient = ?, Quantite = ?, Date = ? WHERE Id = ?";
                            try (Connection cn = DB.getConnection();
                                 PreparedStatement stmt = cn.prepareStatement(query)) {
                                stmt.setInt(1, modifiedVente.getIdArticle());
                                stmt.setInt(2, modifiedVente.getIdClient());
                                stmt.setInt(3, modifiedVente.getQuantite());
                                stmt.setString(4, modifiedVente.getDate());
                                stmt.setInt(5, vente.getId());
                                int rowsAffected = stmt.executeUpdate();
                                if (rowsAffected > 0) {
                                    System.out.println("Modification réussie de la vente.");
                                    // Actualiser la TableView après la modification
                                    vente.setIdArticle(modifiedVente.getIdArticle());
                                    vente.setIdClient(modifiedVente.getIdClient());
                                    vente.setQuantite(modifiedVente.getQuantite());
                                    vente.setDate(modifiedVente.getDate());
                                    getTableView().refresh();
                                } else {
                                    System.out.println("Aucune modification apportée.");
                                }
                            } catch (SQLException e) {
                                System.out.println("Erreur de modification de la vente.");
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

// Méthode pour supprimer une vente
public Callback<TableColumn<Vente, Void>, TableCell<Vente, Void>> deleteVenteCellFactory() {
    return new Callback<TableColumn<Vente, Void>, TableCell<Vente, Void>>() {
        @Override
        public TableCell<Vente, Void> call(final TableColumn<Vente, Void> param) {
            return new TableCell<Vente, Void>() {
                private final Button deleteButton = new Button("Supprimer");

                {
                    deleteButton.setOnAction(event -> {
                        Vente vente = getTableView().getItems().get(getIndex());
                        // Demander confirmation avant de supprimer
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation de suppression");
                        alert.setHeaderText("Voulez-vous vraiment supprimer cette vente?");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            supprimerVente(vente.getId());
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

// Méthode pour supprimer une vente de la base de données
private void supprimerVente(int idVente) {
    // Supprimer la vente de la base de données
    String query = "DELETE FROM ventes WHERE Id = ?";
    try (Connection cn = DB.getConnection();
         PreparedStatement stmt = cn.prepareStatement(query)) {
        stmt.setInt(1, idVente);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Suppression réussie de la vente.");
            // Actualiser la TableView après la suppression
            loadVentes();
        } else {
            System.out.println("Aucune vente n'a été supprimée.");
        }
    } catch (SQLException e) {
        System.out.println("Erreur lors de la suppression de la vente.");
        e.printStackTrace();
    }
    
            ActualiserVentes();
}
}