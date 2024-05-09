package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.application.Platform;
import javafx.beans.binding.IntegerExpression;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Achat {
    private final IntegerProperty id;
    private final StringProperty refArticle;
    private final IntegerProperty idFournisseur;
    private final IntegerProperty quantite;
    private final ObjectProperty<LocalDate> dateAchat;
    private final DoubleProperty prixUnitaire;
    private final DoubleProperty tva;
    private final DoubleProperty prixTotal;

    @FXML
    private TextField refArticleTextField, idFournisseurTextField, quantiteTextField, prixUnitaireTextField, tvaTextField, prixTotalTextField;

    @FXML
    private TableView<Achat> achatTableView;

    @FXML
    private TableColumn<Achat, Integer> idColumn;

    @FXML
    private TableColumn<Achat, String> refArticleColumn;

    @FXML
    private TableColumn<Achat, Integer> idFournisseurColumn;

    @FXML
    private TableColumn<Achat, Integer> quantiteColumn;

    @FXML
    private TableColumn<Achat, LocalDate> dateAchatColumn;

    @FXML
    private TableColumn<Achat, Double> prixUnitaireColumn;

    @FXML
    private TableColumn<Achat, Double> tvaColumn;

    @FXML
    private TableColumn<Achat, Double> prixTotalColumn;

    @FXML
    private Button printButton;

    @FXML
    private TextField searchField;

    private ObservableList<Achat> achatList = FXCollections.observableArrayList();

    private ObservableList<Achat> filteredData = FXCollections.observableArrayList();
    

    public Achat() {
        this.id = new SimpleIntegerProperty();
        this.refArticle = new SimpleStringProperty();
        this.idFournisseur = new SimpleIntegerProperty();
        this.quantite = new SimpleIntegerProperty();
        this.dateAchat = new SimpleObjectProperty<>();
        this.prixUnitaire = new SimpleDoubleProperty();
        this.tva = new SimpleDoubleProperty();
        this.prixTotal = new SimpleDoubleProperty();
    }

    public Achat(int id, String refArticle, int idFournisseur, int quantite, LocalDate dateAchat, double prixUnitaire, double tva, double prixTotal) {
        this.id = new SimpleIntegerProperty(id);
        this.refArticle = new SimpleStringProperty(refArticle);
        this.idFournisseur = new SimpleIntegerProperty(idFournisseur);
        this.quantite = new SimpleIntegerProperty(quantite);
        this.dateAchat = new SimpleObjectProperty<>(dateAchat);
        this.prixUnitaire = new SimpleDoubleProperty(prixUnitaire);
        this.tva = new SimpleDoubleProperty(tva);
        this.prixTotal = new SimpleDoubleProperty(prixTotal);
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

    // Getters and setters for refArticle
    public String getRefArticle() {
        return refArticle.get();
    }

    public StringProperty refArticleProperty() {
        return refArticle;
    }

    public void setRefArticle(String refArticle) {
        this.refArticle.set(refArticle);
    }

    // Getters and setters for idFournisseur
    public int getIdFournisseur() {
        return idFournisseur.get();
    }

    public IntegerProperty idFournisseurProperty() {
        return idFournisseur;
    }

    public void setIdFournisseur(int idFournisseur) {
        this.idFournisseur.set(idFournisseur);
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

    // Getters and setters for dateAchat
    public LocalDate getDateAchat() {
        return dateAchat.get();
    }

    public ObjectProperty<LocalDate> dateAchatProperty() {
        return dateAchat;
    }

    public void setDateAchat(LocalDate dateAchat) {
        this.dateAchat.set(dateAchat);
    }

    // Getters and setters for prixUnitaire
    public double getPrixUnitaire() {
        return prixUnitaire.get();
    }

    public DoubleProperty prixUnitaireProperty() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire.set(prixUnitaire);
    }

    // Getters and setters for tva
    public double getTva() {
        return tva.get();
    }

    public DoubleProperty tvaProperty() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva.set(tva);
    }

    // Getters and setters for prixTotal
    public double getPrixTotal() {
        return prixTotal.get();
    }

    public DoubleProperty prixTotalProperty() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal.set(prixTotal);
    }

    // Méthode pour initialiser la TableView
    @FXML
    private void initialize() {
        // Charger des données dans la TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        refArticleColumn.setCellValueFactory(new PropertyValueFactory<>("refArticle"));
        idFournisseurColumn.setCellValueFactory(new PropertyValueFactory<>("idFournisseur"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        dateAchatColumn.setCellValueFactory(new PropertyValueFactory<>("dateAchat"));
        prixUnitaireColumn.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        tvaColumn.setCellValueFactory(new PropertyValueFactory<>("tva"));
        prixTotalColumn.setCellValueFactory(new PropertyValueFactory<>("prixTotal"));

        // Appliquer les données non filtrées à la TableView
        achatTableView.setItems(loadAchats());

        // Ajouter un écouteur sur le champ de recherche pour filtrer les données
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterData(newValue));
    }

    // Méthode pour filtrer les données dans la TableView
    private void filterData(String newValue) {
        filteredData.clear();
        if (newValue == null || newValue.isEmpty()) {
            filteredData.addAll(achatList);
        } else {
            String lowerCaseFilter = newValue.toLowerCase();
            for (Achat achat : achatList) {
                if (String.valueOf(achat.getId()).toLowerCase().contains(lowerCaseFilter) ||
                        achat.getRefArticle().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(achat.getIdFournisseur()).toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(achat.getQuantite()).toLowerCase().contains(lowerCaseFilter) ||
                        achat.getDateAchat().toString().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(achat.getPrixUnitaire()).toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(achat.getTva()).toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(achat.getPrixTotal()).toLowerCase().contains(lowerCaseFilter)) {
                    filteredData.add(achat);
                }
            }
        }
        achatTableView.setItems(filteredData);
    }

    // Méthode pour charger les achats depuis la base de données
    private ObservableList<Achat> loadAchats() {
        ObservableList<Achat> achatList = FXCollections.observableArrayList();
        String query = "SELECT * FROM achats";
        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("Id");
                String refArticle = rs.getString("Ref_Article");
                int idFournisseur = rs.getInt("Id_Fournisseur");
                int quantite = rs.getInt("Quantite");
                LocalDate dateAchat = rs.getDate("Date_Achat").toLocalDate();
                double prixUnitaire = rs.getDouble("Prix_Unitaire");
                double tva = rs.getDouble("TVA");
                double prixTotal = rs.getDouble("Prix_Total");

                Achat achat = new Achat(id, refArticle, idFournisseur, quantite, dateAchat, prixUnitaire, tva, prixTotal);
                achatList.add(achat);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return achatList;
    }

    // Méthode pour enregistrer un nouvel achat
    @FXML
        void nouvelAchat(ActionEvent event) {
            IntegerProperty idFournisseur = new SimpleIntegerProperty(0);
        // Créer une boîte de dialogue pour la saisie d'un nouvel achat
        Dialog<Achat> dialog = new Dialog<>();
        dialog.setTitle("Nouvel achat");
        dialog.setHeaderText("Saisir les informations du nouvel achat");

        // Définir le bouton Enregistrer
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Créer les champs de saisie pour les nouvelles informations de l'achat
        TextField referenceField = new TextField();
        TextField libelleField = new TextField();
        TextField quantiteField = new TextField();
        TextField prixUnitaireField = new TextField();


        ComboBox<Fournisseur> fournisseurComboBox = new ComboBox<>();
        ObservableList<Fournisseur> fournisseurs = Fournisseur.loadFournisseurs();
        fournisseurComboBox.setItems(fournisseurs);
        
        // Utiliser une cell factory pour afficher les noms des fournisseurs dans le ComboBox
        fournisseurComboBox.setCellFactory(new Callback<ListView<Fournisseur>, ListCell<Fournisseur>>() {
            @Override
            public ListCell<Fournisseur> call(ListView<Fournisseur> param) {
                return new ListCell<Fournisseur>() {
                    @Override
                    protected void updateItem(Fournisseur item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getNom());
                        }
                    }
                };
            }
        });
        
        // Obtenir l'ID du fournisseur sélectionné
        fournisseurComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                idFournisseur.set(newValue.getId());
                System.out.println(idFournisseur.intValue());
                // Faites ce que vous devez faire avec l'ID du fournisseur sélectionné
            }
        });
        


        DatePicker dateAchatPicker = new DatePicker();
        TextField tvaField = new TextField();
        TextField prixTotalField = new TextField();

        // Ajouter les champs de saisie à la boîte de dialogue
        GridPane grid = new GridPane();
        grid.add(new Label("Référence article:"), 0, 0);
        grid.add(referenceField, 1, 0);
        grid.add(new Label("Libellé article:"), 0, 1);
        grid.add(libelleField, 1, 1);
        grid.add(new Label("Quantité:"), 0, 2);
        grid.add(quantiteField, 1, 2);
        grid.add(new Label("Prix unitaire:"), 0, 3);
        grid.add(prixUnitaireField, 1, 3);
        grid.add(new Label("Fournisseur:"), 0, 4);
        grid.add(fournisseurComboBox, 1, 4);
        grid.add(new Label("Date achat:"), 0, 5);
        grid.add(dateAchatPicker, 1, 5);
        grid.add(new Label("TVA:"), 0, 6);
        grid.add(tvaField, 1, 6);
        grid.add(new Label("Prix total:"), 0, 7);
        grid.add(prixTotalField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        // Demander à la boîte de dialogue de se concentrer sur le champ de référence article par défaut
        Platform.runLater(() -> referenceField.requestFocus());

        // Convertir le résultat de la boîte de dialogue en objet Achat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Créer un nouvel achat avec les nouvelles informations
// Achat(int id, String refArticle, int idFournisseur, int quantite, LocalDate dateAchat, double prixUnitaire, double tva, double prixTotal)
                Achat nouvelAchat = new Achat(
                        -1,
                        referenceField.getText(),
                        idFournisseur.intValue(),
                        Integer.parseInt(quantiteField.getText()),
                        dateAchatPicker.getValue(),
                        Double.parseDouble(prixUnitaireField.getText()),
                        Double.parseDouble(tvaField.getText()),
                        Double.parseDouble(prixTotalField.getText())
                );

                // enregistrer l'article
               if(dbArticle(referenceField.getText(), libelleField.getText(), Integer.parseInt(quantiteField.getText()),Double.parseDouble(prixUnitaireField.getText()),Double.parseDouble(prixTotalField.getText()))){
                // Enregistrer le nouvel achat dans la base de données
                if (enregistrerAchat(nouvelAchat)) {
                    afficherAlerte("Nouvel achat enregistré avec succès !");
                    achatTableView.setItems(loadAchats());
                } else {
                    afficherAlerte("Erreur lors de l'enregistrement du nouvel achat !");
                }
               }
            }
            return null;
        });

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        dialog.showAndWait();
    }

    private boolean enregistrerAchat(Achat achat) {
        String query = "INSERT INTO achats (ref_article, quantite, date_achat, prix_unitaire, tva, prix_total, id_fournisseur) VALUES (?, ?, ?, ?, ?, ?,?)";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, achat.getRefArticle());
            statement.setInt(2, achat.getQuantite());
            statement.setDate(3, java.sql.Date.valueOf(achat.getDateAchat()));
            statement.setDouble(4, achat.getPrixUnitaire());
            statement.setDouble(5, achat.getTva());
            statement.setDouble(6, achat.getPrixTotal());
            statement.setInt(7, achat.getIdFournisseur());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            afficherAlerte("Erreur lors de l'enregistrement du nouvel achat : " + e.getMessage());
            return false;
        }
    }
    public boolean dbArticle(String reference, String libelle, int stock,double prix_achat,double prix_vente){
        String query = "INSERT INTO articles (reference, libelle, stock, prix_achat, prix_vente) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reference);
            statement.setString(2, libelle);
            statement.setInt(3, stock);
            statement.setDouble(4, prix_achat);
            statement.setDouble(5, prix_vente);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                afficherAlerte("Nouvel article enregistré avec succès !");
                return true;
            } else {
                afficherAlerte("base, Erreur lors de l'enregistrement du nouvel article !");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n Trace:" + e.getStackTrace() + "\nLocalisation"+ e.getLocalizedMessage());
            afficherAlerte("Erreur lors de l'enregistrement du nouvel article : " + e.getMessage());
            return false;
        }
    }






















    @FXML
    public void provision(){
        IntegerProperty idFournisseur = new SimpleIntegerProperty(0);
        IntegerProperty ancienStock = new SimpleIntegerProperty();
        StringProperty refeArticle = new SimpleStringProperty();

        // Créer une boîte de dialogue pour la saisie d'un apprivisionnement
        Dialog<Achat> dialog = new Dialog<>();
        dialog.setTitle("Approvisionnement");
        dialog.setHeaderText("Saisir les informations de l'approvisionnement");

        // Définir le bouton Enregistrer
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Créer les champs de saisie pour les nouvelles informations de l'approvisionnement        
        ComboBox<Article> articleComboBox = new ComboBox<>();
        ObservableList<Article> articles = Article.loadArticles();
        articleComboBox.setItems(articles);
        // Utiliser une cell factory pour afficher les noms des articles dans le ComboBox
        articleComboBox.setCellFactory(new Callback<ListView<Article>, ListCell<Article>>() {
            @Override
            public ListCell<Article> call(ListView<Article> param) {
                return new ListCell<Article>() {
                    @Override
                    protected void updateItem(Article item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getLibelle());
                        }
                    }
                };
            }
        });

         // Obtenir la ref de l'article sélectionné.
         articleComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                refeArticle.set(newValue.getReference());
                ancienStock.set(newValue.getStock());
                System.out.print(refeArticle.getValue() +" "+ ancienStock.intValue());
                // Faites ce que vous devez faire avec l'ID du fournisseur sélectionné
            }
        });

        TextField quantiteField = new TextField();
        TextField prixUnitaireField = new TextField();

        //Definir la liste deroulante pour selectionner le fournisseur.
        ComboBox<Fournisseur> fournisseurComboBox = new ComboBox<>();
        ObservableList<Fournisseur> fournisseurs = Fournisseur.loadFournisseurs();
        fournisseurComboBox.setItems(fournisseurs);
        
        // Utiliser une cell factory pour afficher les noms des fournisseurs dans le ComboBox
        fournisseurComboBox.setCellFactory(new Callback<ListView<Fournisseur>, ListCell<Fournisseur>>() {
            @Override
            public ListCell<Fournisseur> call(ListView<Fournisseur> param) {
                return new ListCell<Fournisseur>() {
                    @Override
                    protected void updateItem(Fournisseur item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getNom());
                        }
                    }
                };
            }
        });

        
        
        // Obtenir l'ID du fournisseur sélectionné
        fournisseurComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                idFournisseur.set(newValue.getId());
                System.out.println(idFournisseur.intValue());
                // Faites ce que vous devez faire avec l'ID du fournisseur sélectionné
            }
        });
        


        DatePicker dateAchatPicker = new DatePicker();
        TextField tvaField = new TextField();
        TextField prixTotalField = new TextField();

        // Ajouter les champs de saisie à la boîte de dialogue
        GridPane grid = new GridPane();
        grid.add(new Label("Référence article:"), 0, 0);
        grid.add(articleComboBox, 1, 0);
        grid.add(new Label("Quantité:"), 0, 2);
        grid.add(quantiteField, 1, 2);
        grid.add(new Label("Prix unitaire:"), 0, 3);
        grid.add(prixUnitaireField, 1, 3);
        grid.add(new Label("Fournisseur:"), 0, 4);
        grid.add(fournisseurComboBox, 1, 4);
        grid.add(new Label("Date achat:"), 0, 5);
        grid.add(dateAchatPicker, 1, 5);
        grid.add(new Label("TVA:"), 0, 6);
        grid.add(tvaField, 1, 6);
        grid.add(new Label("Prix total:"), 0, 7);
        grid.add(prixTotalField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        // Demander à la boîte de dialogue de se concentrer sur le champ de référence article par défaut
        Platform.runLater(() -> articleComboBox.requestFocus());

        // Convertir le résultat de la boîte de dialogue en objet Achat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Créer un nouvel achat avec les nouvelles informations
// Achat(int id, String refArticle, int idFournisseur, int quantite, LocalDate dateAchat, double prixUnitaire, double tva, double prixTotal)
                Achat nouvelAchat = new Achat(
                        -1,
                        refeArticle.getValue(),
                        idFournisseur.intValue(),
                        Integer.parseInt(quantiteField.getText()),
                        dateAchatPicker.getValue(),
                        Double.parseDouble(prixUnitaireField.getText()),
                        Double.parseDouble(tvaField.getText()),
                        Double.parseDouble(prixTotalField.getText())
                );

                // enregistrer l'article
               if(updateArticle(Integer.parseInt(quantiteField.getText())+ancienStock.intValue(), refeArticle.getValue())){
                // Enregistrer le nouvel achat dans la base de données
                if (enregistrerAchat(nouvelAchat)) {
                    afficherAlerte("Nouvel approvisionnement enregistré avec succès !");
                    achatTableView.setItems(loadAchats());
                } else {
                    afficherAlerte("Erreur lors de l'enregistrement du nouvel approvisionnement !");
                }
               }
            }
            return null;
        });

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        dialog.showAndWait();

    }






    public boolean updateArticle(int stock, String reference){
        String query = "UPDATE articles SET stock = ? WHERE reference = ?";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, stock);
            statement.setString(2, reference);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                afficherAlerte("Stock augmenté avec succès !");
                return true;
            } else {
                afficherAlerte("base, Erreur lors de l'enregistrement du nouvel approvisionnement !");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n Trace:" + e.getStackTrace() + "\nLocalisation"+ e.getLocalizedMessage());
            afficherAlerte("Erreur lors de l'enregistrement du nouvel approvisionnement : " + e.getMessage());
            return false;
        }
    }

















    private void afficherAlerte(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void printAchats(ActionEvent event) {
    try  {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            // Titre du document
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Liste des achats");
            contentStream.endText();

            // Contenu des ventes
            int y = 650; // Position verticale initiale
            for (Achat achat : achatList) {
                String fournisseurText = achat.getId()+ " - " + achat.getRefArticle() + " - " + achat.getIdFournisseur() + " - " +
                achat.getPrixUnitaire()+  " - " + achat.getTva() + " - " +
                achat.getQuantite()+  " - " + achat.getPrixTotal();
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
        File file = new File(home + "/Downloads/ListeAchats"+dateEtHeureFormatees+".pdf");
        document.save(file);

        System.out.println("Le fichier PDF a été générée avec succès.");
        afficherAlerte("La liste des achats a été générée avec succès dans votre dossier de telechargement courant.");
    } catch (IOException e) {
        System.err.println("Erreur lors de la génération du fichier PDF : " + e.getMessage());
    }
}

}

