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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import java.io.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {
    private final IntegerProperty id;
    private final StringProperty nom;
    private final StringProperty adresse;
    private final StringProperty telephone;

    @FXML
    private TextField nomTextField, adresseTextField, telephoneTextField;

    @FXML
    private TableView<Client> clientTableView;

    @FXML
    private TableColumn<Client, Integer> idColumn;

    @FXML
    private TableColumn<Client, String> nomColumn;

    @FXML
    private TableColumn<Client, String> adresseColumn;

    @FXML
    private TableColumn<Client, String> telephoneColumn;

    @FXML
    private Button printButton;

    @FXML
    private TextField csearchField;

    private ObservableList<Client> clientList = FXCollections.observableArrayList();

    private ObservableList<Client> filteredData = FXCollections.observableArrayList();

    public Client() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.adresse = new SimpleStringProperty();
        this.telephone = new SimpleStringProperty();
    }

    public Client(int id, String nom, String adresse, String telephone) {
        this.id = new SimpleIntegerProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.adresse = new SimpleStringProperty(adresse);
        this.telephone = new SimpleStringProperty(telephone);
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

    // Load clients from the database
    private ObservableList<Client> loadClients() {
        String query = "SELECT * FROM clients";
        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("Id");
                String nom = rs.getString("Nom");
                String adresse = rs.getString("Adresse");
                String telephone = rs.getString("Telephone");

                Client client = new Client(id, nom, adresse, telephone);
                clientList.add(client);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return clientList;
    }

    // Initialize method
    @FXML
    private void initialize() {
        // Load clients into the TableView
        ObservableList<Client> clients = loadClients();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        // Add custom columns with edit and delete buttons
        TableColumn<Client, Void> editColumn = new TableColumn<>("Modifier");
        editColumn.setCellFactory(editClientCellFactory());
        clientTableView.getColumns().add(editColumn);

        TableColumn<Client, Void> deleteColumn = new TableColumn<>("Supprimer");
        deleteColumn.setCellFactory(deleteClientCellFactory());
        clientTableView.getColumns().add(deleteColumn);

        // Apply unfiltered data to the TableView
        clientTableView.setItems(clients);

        // Add listener to the search field to filter data
        csearchField.textProperty().addListener((observable, oldValue, newValue) -> filterData(newValue));
    }

    // Filter data based on search text
    private void filterData(String newValue) {
        filteredData.clear();
        if (newValue == null || newValue.isEmpty()) {
            filteredData.addAll(clientList);
        } else {
            String lowerCaseFilter = newValue.toLowerCase();
            for (Client client : clientList) {
                if (String.valueOf(client.getId()).toLowerCase().contains(lowerCaseFilter) ||
                        client.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        client.getAdresse().toLowerCase().contains(lowerCaseFilter) ||
                        client.getTelephone().toLowerCase().contains(lowerCaseFilter)) {
                    filteredData.add(client);
                }
            }
        }
        clientTableView.setItems(filteredData);
    }

    // Save client data
    @FXML
    void enregistrerClient(ActionEvent event) {
        String nom = nomTextField.getText();
        String adresse = adresseTextField.getText();
        String telephone = telephoneTextField.getText();

        String query = "INSERT INTO clients (Nom, Adresse, Telephone) VALUES (?, ?, ?)";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nom);
            statement.setString(2, adresse);
            statement.setString(3, telephone);
            statement.executeUpdate();
            System.out.println("Données insérées avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion des données : " + e.getMessage());
        }

        // Refresh TableView after insertion
        clientTableView.getItems().clear();
        loadClients();
    }

    @FXML
    void nouveauClient(ActionEvent event) {
        // Créer une boîte de dialogue pour la saisie d'un nouveau client
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("Nouveau client");
        dialog.setHeaderText("Saisir les informations du nouveau client");

        // Définir le bouton Enregistrer
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Créer les champs de saisie pour les nouvelles informations du client
        TextField nomField = new TextField();
        TextField adresseField = new TextField();
        TextField telephoneField = new TextField();

        // Ajouter les champs de saisie à la boîte de dialogue
        GridPane grid = new GridPane();
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Adresse:"), 0, 1);
        grid.add(adresseField, 1, 1);
        grid.add(new Label("Téléphone:"), 0, 2);
        grid.add(telephoneField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Demander à la boîte de dialogue de se concentrer sur le champ de nom par défaut
        Platform.runLater(() -> nomField.requestFocus());

        // Convertir le résultat de la boîte de dialogue en objet Client
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Créer un nouveau client avec les nouvelles informations
                Client nouveauClient = new Client();
                nouveauClient.setNom(nomField.getText());
                nouveauClient.setAdresse(adresseField.getText());
                nouveauClient.setTelephone(telephoneField.getText());
                return nouveauClient;
            }
            return null;
        });

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        Optional<Client> result = dialog.showAndWait();

        // Si l'utilisateur clique sur le bouton Enregistrer, enregistrer le nouveau client
        result.ifPresent(nouveauClient -> {
            String query = "INSERT INTO clients (Nom, Adresse, Telephone) VALUES (?, ?, ?)";
            try (Connection connection = DB.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, nouveauClient.getNom());
                statement.setString(2, nouveauClient.getAdresse());
                statement.setString(3, nouveauClient.getTelephone());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    afficherAlerte("Nouveau client enregistré avec succès !");
                    // Actualiser la TableView après l'insertion
                    clientList.clear();
                    clientList.addAll(loadClients());
                } else {
                    afficherAlerte("Erreur lors de l'enregistrement du nouveau client !");
                }
            } catch (SQLException e) {
                afficherAlerte("Erreur lors de l'enregistrement du nouveau client : " + e.getMessage());
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


    // Clear input fields
    @FXML
    void annuler(ActionEvent event) {
        nomTextField.clear();
        adresseTextField.clear();
        telephoneTextField.clear();
    }

    // Method to create custom cell with delete button
    public Callback<TableColumn<Client, Void>, TableCell<Client, Void>> deleteClientCellFactory() {
        return new Callback<TableColumn<Client, Void>, TableCell<Client, Void>>() {
            @Override
            public TableCell<Client, Void> call(final TableColumn<Client, Void> param) {
                return new TableCell<Client, Void>() {
                    private final Button deleteButton = new Button("Supprimer");

                    {
                        deleteButton.setOnAction(event -> {
                            Client client = getTableView().getItems().get(getIndex());
                            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                            confirmationDialog.setTitle("Confirmation de suppression");
                            confirmationDialog.setHeaderText("Supprimer le client ?");
                            confirmationDialog.setContentText("Êtes-vous sûr de vouloir supprimer le client : " + client.getNom() + " ?");
                            Optional<ButtonType> result = confirmationDialog.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                String query = "DELETE FROM clients WHERE Id = ?";
                                try (Connection cn = DB.getConnection();
                                     PreparedStatement stmt = cn.prepareStatement(query)) {
                                    stmt.setInt(1, client.getId());
                                    int rowsAffected = stmt.executeUpdate();
                                    if (rowsAffected > 0) {
                                        System.out.println("Suppression réussie du client : " + client.getNom());
                                        getTableView().getItems().remove(client);
                                    } else {
                                        System.out.println("Aucun client supprimé.");
                                    }
                                } catch (SQLException e) {
                                    System.out.println("Erreur de suppression du client : " + client.getNom());
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

    // Method to create custom cell with edit button
    public Callback<TableColumn<Client, Void>, TableCell<Client, Void>> editClientCellFactory() {
        return new Callback<TableColumn<Client, Void>, TableCell<Client, Void>>() {
            @Override
            public TableCell<Client, Void> call(final TableColumn<Client, Void> param) {
                return new TableCell<Client, Void>() {
                    private final Button editButton = new Button("Modifier");

                    {
                        editButton.setOnAction(event -> {
                            Client client = getTableView().getItems().get(getIndex());
                            Dialog<Client> dialog = new Dialog<>();
                            dialog.setTitle("Modifier le client");
                            dialog.setHeaderText("Modifier les informations du client : " + client.getNom());
                            ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
                            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
                            TextField nomField = new TextField(client.getNom());
                            TextField adresseField = new TextField(client.getAdresse());
                            TextField telephoneField = new TextField(client.getTelephone());
                            GridPane grid = new GridPane();
                            grid.add(new Label("Nom:"), 0, 0);
                            grid.add(nomField, 1, 0);
                            grid.add(new Label("Adresse:"), 0, 1);
                            grid.add(adresseField, 1, 1);
                            grid.add(new Label("Téléphone:"), 0, 2);
                            grid.add(telephoneField, 1, 2);
                            dialog.getDialogPane().setContent(grid);
                            Platform.runLater(() -> nomField.requestFocus());
                            dialog.setResultConverter(dialogButton -> {
                                if (dialogButton == saveButtonType) {
                                    Client editedClient = new Client();
                                    editedClient.setNom(nomField.getText());
                                    editedClient.setAdresse(adresseField.getText());
                                    editedClient.setTelephone(telephoneField.getText());
                                    return editedClient;
                                }
                                return null;
                            });
                            Optional<Client> result = dialog.showAndWait();
                            result.ifPresent(editedClient -> {
                                String query = "UPDATE clients SET Nom = ?, Adresse = ?, Telephone = ? WHERE Id = ?";
                                try (Connection cn = DB.getConnection();
                                        PreparedStatement stmt = cn.prepareStatement(query)) {
                                    stmt.setString(1, editedClient.getNom());
                                    stmt.setString(2, editedClient.getAdresse());
                                    stmt.setString(3, editedClient.getTelephone());
                                    stmt.setInt(4, client.getId());
                                    int rowsAffected = stmt.executeUpdate();
                                    if (rowsAffected > 0) {
                                        System.out.println("Modification réussie du client : " + client.getNom());
                                        client.setNom(editedClient.getNom());
                                        client.setAdresse(editedClient.getAdresse());
                                        client.setTelephone(editedClient.getTelephone());
                                        getTableView().refresh();
                                    } else {
                                        System.out.println("Aucune modification apportée.");
                                    }
                                } catch (SQLException e) {
                                    System.out.println("Erreur de modification du client : " + client.getNom());
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

    @FXML
    void printClients(ActionEvent event) {
    try  {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            // Titre du document
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Liste des Clients");
            contentStream.endText();

            // Contenu des ventes
            int y = 650; // Position verticale initiale
            for (Client client : clientList) {
                String clientText = client.getId() + " - " + client.getNom() + " - " +
                                  client.getTelephone() + " - " + client.getAdresse();
                contentStream.beginText();
                contentStream.newLineAtOffset(100, y);
                contentStream.showText(clientText);
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
        File file = new File(home + "/Downloads/ListeClients"+dateEtHeureFormatees+".pdf");
        document.save(file);

        System.out.println("Le fichier PDF a été généré avec succès.");
        afficherAlerte("La liste des clients a été générée avec succès dans votre dossier de telechargement courant.");
    } catch (IOException e) {
        System.err.println("Erreur lors de la génération du fichier PDF : " + e.getMessage());
    }

    }
}
