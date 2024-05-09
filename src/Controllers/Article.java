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
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Article {
    private final IntegerProperty id;
    private final StringProperty reference;
    private final StringProperty libelle;
    private final IntegerProperty stock;
    private final DoubleProperty prixAchat;
    private final DoubleProperty prixVente;

    @FXML
    private TextField referenceTextField, libelleTextField, stockTextField, prixAchatTextField, prixVenteTextField;

    @FXML
    private TableView<Article> articleTableView;

    @FXML
    private TableColumn<Article, Integer> idColumn;

    @FXML
    private TableColumn<Article, String> referenceColumn, libelleColumn;

    @FXML
    private TableColumn<Article, Integer> stockColumn;

    @FXML
    private TableColumn<Article, Double> prixAchatColumn, prixVenteColumn;

    @FXML
    private Button printButton;

    @FXML
    private TextField searchField;

    private static ObservableList<Article> articleList = FXCollections.observableArrayList();

    private ObservableList<Article> filteredData = FXCollections.observableArrayList();

    public Article() {
        this.id = new SimpleIntegerProperty();
        this.reference = new SimpleStringProperty();
        this.libelle = new SimpleStringProperty();
        this.stock = new SimpleIntegerProperty();
        this.prixAchat = new SimpleDoubleProperty();
        this.prixVente = new SimpleDoubleProperty();
    }

    public Article(int id, String reference, String libelle, int stock, double prixAchat, double prixVente) {
        this.id = new SimpleIntegerProperty(id);
        this.reference = new SimpleStringProperty(reference);
        this.libelle = new SimpleStringProperty(libelle);
        this.stock = new SimpleIntegerProperty(stock);
        this.prixAchat = new SimpleDoubleProperty(prixAchat);
        this.prixVente = new SimpleDoubleProperty(prixVente);
    }

    public Article(int id, String reference, String libelle) {
        this.id = new SimpleIntegerProperty(id);
        this.reference = new SimpleStringProperty(reference);
        this.libelle = new SimpleStringProperty(libelle);
        this.stock = new SimpleIntegerProperty();
        this.prixAchat = new SimpleDoubleProperty();
        this.prixVente = new SimpleDoubleProperty();
    }

    @Override
    public String toString() {
        return this.getLibelle(); // Retourne le libellé de l'article comme représentation dans le ComboBox
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

    // Getters and setters for reference
    public String getReference() {
        return reference.get();
    }

    public StringProperty referenceProperty() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference.set(reference);
    }

    // Getters and setters for libelle
    public String getLibelle() {
        return libelle.get();
    }

    public StringProperty libelleProperty() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle.set(libelle);
    }

    // Getters and setters for stock
    public int getStock() {
        return stock.get();
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    // Getters and setters for prixAchat
    public double getPrixAchat() {
        return prixAchat.get();
    }

    public DoubleProperty prixAchatProperty() {
        return prixAchat;
    }

    public void setPrixAchat(double prixAchat) {
        this.prixAchat.set(prixAchat);
    }

    // Getters and setters for prixVente
    public double getPrixVente() {
        return prixVente.get();
    }

    public DoubleProperty prixVenteProperty() {
        return prixVente;
    }

    public void setPrixVente(double prixVente) {
        this.prixVente.set(prixVente);
    }


    // Méthode pour initialiser la classe
    @FXML
    private void initialize() {
        // Charger des données dans la TableView
        ObservableList<Article> articles = loadArticles();
        idColumn.setCellValueFactory(new PropertyValueFactory<Article, Integer>("id"));
        referenceColumn.setCellValueFactory(new PropertyValueFactory<Article, String>("reference"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<Article, String>("libelle"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<Article, Integer>("stock"));
        prixAchatColumn.setCellValueFactory(new PropertyValueFactory<Article, Double>("prixAchat"));
        prixVenteColumn.setCellValueFactory(new PropertyValueFactory<Article, Double>("prixVente"));

        // Ajouter une colonne personnalisée avec un bouton de modification
        TableColumn<Article, Void> editColumn = new TableColumn<>("Modifier");
        editColumn.setCellFactory(editArticleCellFactory());
        articleTableView.getColumns().add(editColumn);
   
        //Supprimer les Informations.
        TableColumn<Article, Void> deleteColumn = new TableColumn<>("Supprimer");
        deleteColumn.setCellFactory(deleteArticleCellFactory());
        articleTableView.getColumns().add(deleteColumn);

        // Appliquer les données à la TableView
        articleTableView.setItems(articles);

        // Ajouter un écouteur sur le champ de recherche pour filtrer les données
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterData(newValue));
    }

    // Méthode pour charger les données des articles depuis la base de données
    public static ObservableList<Article> loadArticles() {
        String query = "SELECT * FROM articles";
        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("Id");
                String reference = rs.getString("reference");
                String libelle = rs.getString("libelle");
                int stock = rs.getInt("stock");
                double prixAchat = rs.getDouble("prix_achat");
                double prixVente = rs.getDouble("prix_vente");

                Article article = new Article(id, reference, libelle, stock, prixAchat, prixVente);
                articleList.add(article);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return articleList;
    }

    // Méthode pour filtrer les données des articles
    private void filterData(String newValue) {
        filteredData.clear();
        if (newValue == null || newValue.isEmpty()) {
            filteredData.addAll(articleList);
        } else {
            String lowerCaseFilter = newValue.toLowerCase();
            for (Article article : articleList) {
                if (String.valueOf(article.getId()).toLowerCase().contains(lowerCaseFilter) ||
                        article.getReference().toLowerCase().contains(lowerCaseFilter) ||
                        article.getLibelle().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(article.getStock()).toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(article.getPrixAchat()).toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(article.getPrixVente()).toLowerCase().contains(lowerCaseFilter)) {
                    filteredData.add(article);
                }
            }
        }
        articleTableView.setItems(filteredData);
    }

    // Méthode pour imprimer les articles
    @FXML
    void printArticles(ActionEvent event) {
    try  {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            // Titre du document
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Liste des Articles");
            contentStream.endText();

            // Contenu des ventes
            int y = 650; // Position verticale initiale
            for (Article article : articleList) {
                String articleText = article.getId()+ " - " + article.getReference() + " - " + article.getLibelle() + " - " +
                                  article.getPrixAchat()+  " - " + article.getStock();
                contentStream.beginText();
                contentStream.newLineAtOffset(100, y);
                contentStream.showText(articleText);
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
        File file = new File(home + "/Downloads/ListeArticles"+dateEtHeureFormatees+".pdf");
        document.save(file);

        System.out.println("Le fichier PDF a été généré avec succès.");
        afficherAlerte("La liste des Articles a été généré avec succès dans votre dossier de telechargement courant.");
    } catch (IOException e) {
        System.err.println("Erreur lors de la génération du fichier PDF : " + e.getMessage());
    }

    }
    

    @FXML
void nouvelArticle(ActionEvent event) {
    // Créer une boîte de dialogue pour la saisie d'un nouvel article
    Dialog<Article> dialog = new Dialog<>();
    dialog.setTitle("Nouvel article");
    dialog.setHeaderText("Saisir les informations du nouvel article");

    // Définir le bouton Enregistrer
    ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

    // Créer les champs de saisie pour les nouvelles informations de l'article
    TextField referenceField = new TextField();
    TextField libelleField = new TextField();
    TextField stockField = new TextField();
    TextField prixAchatField = new TextField();
    TextField prixVenteField = new TextField();

    // Ajouter les champs de saisie à la boîte de dialogue
    GridPane grid = new GridPane();
    grid.add(new Label("Référence:"), 0, 0);
    grid.add(referenceField, 1, 0);
    grid.add(new Label("Libellé:"), 0, 1);
    grid.add(libelleField, 1, 1);
    grid.add(new Label("Stock:"), 0, 2);
    grid.add(stockField, 1, 2);
    grid.add(new Label("Prix Achat:"), 0, 3);
    grid.add(prixAchatField, 1, 3);
    grid.add(new Label("Prix Vente:"), 0, 4);
    grid.add(prixVenteField, 1, 4);

    dialog.getDialogPane().setContent(grid);

    // Demander à la boîte de dialogue de se concentrer sur le champ de référence par défaut
    Platform.runLater(() -> referenceField.requestFocus());

    // Convertir le résultat de la boîte de dialogue en objet Article
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == saveButtonType) {
            // Créer un nouvel article avec les nouvelles informations
            Article nouvelArticle = new Article();
            nouvelArticle.setReference(referenceField.getText());
            nouvelArticle.setLibelle(libelleField.getText());
            nouvelArticle.setStock(Integer.parseInt(stockField.getText()));
            nouvelArticle.setPrixAchat(Double.parseDouble(prixAchatField.getText()));
            nouvelArticle.setPrixVente(Double.parseDouble(prixVenteField.getText()));
            return nouvelArticle;
        }
        return null;
    });

    // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
    Optional<Article> result = dialog.showAndWait();

    // Si l'utilisateur clique sur le bouton Enregistrer, enregistrer le nouvel article
    result.ifPresent(nouvelArticle -> {
        dbArticle(nouvelArticle.getReference(),nouvelArticle.getLibelle(), nouvelArticle.getStock(),nouvelArticle.getPrixAchat(), nouvelArticle.getPrixVente());
    });
}
 
    public void dbArticle(String reference, String libelle, int stock,double prix_achat,double prix_vente){
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
                // Actualiser la TableView après l'insertion
                articleList.clear();
                articleList.addAll();
            } else {
                afficherAlerte("base, Erreur lors de l'enregistrement du nouvel article !");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n Trace:" + e.getStackTrace() + "\nLocalisation"+ e.getLocalizedMessage());
            afficherAlerte("Erreur lors de l'enregistrement du nouvel article : " + e.getMessage());
        }
    }

private void afficherAlerte(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Info");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

// Méthode pour créer une cellule personnalisée avec un bouton de suppression
public Callback<TableColumn<Article, Void>, TableCell<Article, Void>> deleteArticleCellFactory() {
    return new Callback<TableColumn<Article, Void>, TableCell<Article, Void>>() {
        @Override
        public TableCell<Article, Void> call(final TableColumn<Article, Void> param) {
            return new TableCell<Article, Void>() {
                private final Button deleteButton = new Button("Supprimer");

                {
                    deleteButton.setOnAction(event -> {
                        Article article = getTableView().getItems().get(getIndex());

                        // Créer une boîte de dialogue de confirmation
                        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmationDialog.setTitle("Confirmation de suppression");
                        confirmationDialog.setHeaderText("Supprimer l'article ?");
                        confirmationDialog.setContentText("Êtes-vous sûr de vouloir supprimer l'article : " + article.getLibelle() + " ?");

                        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                        Optional<ButtonType> result = confirmationDialog.showAndWait();

                        // Si l'utilisateur clique sur le bouton OK, supprimer l'article
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            String query = "DELETE FROM articles WHERE Id = ?";
                            try (Connection cn = DB.getConnection();
                                 PreparedStatement stmt = cn.prepareStatement(query)) {
                                stmt.setInt(1, article.getId());
                                int rowsAffected = stmt.executeUpdate();
                                if (rowsAffected > 0) {
                                    System.out.println("Suppression réussie de l'article : " + article.getLibelle());
                                    // Actualiser la TableView après la suppression
                                    getTableView().getItems().remove(article);
                                } else {
                                    System.out.println("Aucun article supprimé.");
                                }
                            } catch (SQLException e) {
                                System.out.println("Erreur de suppression de l'article : " + article.getLibelle());
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

// Méthode pour créer une cellule personnalisée avec un bouton de modification
public Callback<TableColumn<Article, Void>, TableCell<Article, Void>> editArticleCellFactory() {
    return new Callback<TableColumn<Article, Void>, TableCell<Article, Void>>() {
        @Override
        public TableCell<Article, Void> call(final TableColumn<Article, Void> param) {
            return new TableCell<Article, Void>() {
                private final Button editButton = new Button("Modifier");

                {
                    editButton.setOnAction(event -> {
                        Article article = getTableView().getItems().get(getIndex());

                        // Créer une boîte de dialogue pour la modification de l'article
                        Dialog<Article> dialog = new Dialog<>();
                        dialog.setTitle("Modifier l'article");
                        dialog.setHeaderText("Modifier les informations de l'article : " + article.getLibelle());

                        // Définir le bouton Enregistrer
                        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                        // Créer les champs de saisie pour les nouvelles informations de l'article
                        TextField referenceField = new TextField(article.getReference());
                        TextField libelleField = new TextField(article.getLibelle());
                        TextField stockField = new TextField(String.valueOf(article.getStock()));
                        TextField prixAchatField = new TextField(String.valueOf(article.getPrixAchat()));
                        TextField prixVenteField = new TextField(String.valueOf(article.getPrixVente()));

                        // Ajouter les champs de saisie à la boîte de dialogue
                        GridPane grid = new GridPane();
                        grid.add(new Label("Référence:"), 0, 0);
                        grid.add(referenceField, 1, 0);
                        grid.add(new Label("Libellé:"), 0, 1);
                        grid.add(libelleField, 1, 1);
                        grid.add(new Label("Stock:"), 0, 2);
                        grid.add(stockField, 1, 2);
                        grid.add(new Label("Prix Achat:"), 0, 3);
                        grid.add(prixAchatField, 1, 3);
                        grid.add(new Label("Prix Vente:"), 0, 4);
                        grid.add(prixVenteField, 1, 4);

                        dialog.getDialogPane().setContent(grid);

                        // Demander à la boîte de dialogue de se concentrer sur le champ de référence par défaut
                        Platform.runLater(() -> referenceField.requestFocus());

                        // Convertir le résultat de la boîte de dialogue en objet Article
                        dialog.setResultConverter(dialogButton -> {
                            if (dialogButton == saveButtonType) {
                                // Créer un nouvel article avec les nouvelles informations
                                Article editedArticle = new Article();
                                editedArticle.setReference(referenceField.getText());
                                editedArticle.setLibelle(libelleField.getText());
                                editedArticle.setStock(Integer.parseInt(stockField.getText()));
                                editedArticle.setPrixAchat(Double.parseDouble(prixAchatField.getText()));
                                editedArticle.setPrixVente(Double.parseDouble(prixVenteField.getText()));
                                return editedArticle;
                            }
                            return null;
                        });

                        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                        Optional<Article> result = dialog.showAndWait();

                        // Si l'utilisateur clique sur le bouton Enregistrer, enregistrer les modifications
                        result.ifPresent(editedArticle -> {
                            // Enregistrer les modifications dans la base de données
                            String query = "UPDATE articles SET Reference = ?, Libelle = ?, Stock = ?, Prix_Achat = ?, Prix_Vente = ? WHERE id = ?";
                            try (Connection cn = DB.getConnection();
                                    PreparedStatement stmt = cn.prepareStatement(query)) {
                                stmt.setString(1, editedArticle.getReference());
                                stmt.setString(2, editedArticle.getLibelle());
                                stmt.setInt(3, editedArticle.getStock());
                                stmt.setDouble(4, editedArticle.getPrixAchat());
                                stmt.setDouble(5, editedArticle.getPrixVente());
                                stmt.setInt(6, article.getId());
                                int rowsAffected = stmt.executeUpdate();
                                if (rowsAffected > 0) {
                                    System.out.println("Modification réussie de l'article : " + article.getLibelle());
                                    // Actualiser la TableView après la modification
                                    article.setReference(editedArticle.getReference());
                                    article.setLibelle(editedArticle.getLibelle());
                                    article.setStock(editedArticle.getStock());
                                    article.setPrixAchat(editedArticle.getPrixAchat());
                                    article.setPrixVente(editedArticle.getPrixVente());
                                    getTableView().refresh();
                                } else {
                                    System.out.println("Aucune modification apportée.");
                                }
                            } catch (SQLException e) {
                                System.out.println("Erreur de modification de l'article : " + article.getLibelle());
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
