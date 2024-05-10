package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.Random;

public class Notification {
    private IntegerProperty id;
    private StringProperty message;
    private StringProperty type;
    private StringProperty date;

    public Notification(){
        this.id = new SimpleIntegerProperty();
        this.message = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
        this.date = new SimpleStringProperty();
    }

    public Notification( int id, String message, String type, String date){
        this.id = new SimpleIntegerProperty(id);
        this.message = new SimpleStringProperty(message);
        this.type = new SimpleStringProperty(type);
        this.date = new SimpleStringProperty(date);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }


    @FXML
    private ScrollPane notificationsScrollPane;

    public void initialize() {
        ajouterNotifications();
    }

    public void ajouterNotifications() {
        VBox notificationsPane = new VBox(); // Utilisation d'un VBox pour empiler les notifications
        notificationsPane.setSpacing(10); // Espacement entre chaque notification

        // Connexion à la base de données pour récupérer les articles dont le stock est inférieur à la moitié
        String query = "SELECT * FROM articles ";
        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                // Récupérer les informations de l'article
                int id = rs.getInt("Id");
                String reference = rs.getString("reference");
                String libelle = rs.getString("libelle");
                int stock = rs.getInt("stock");

                // Créer un panneau pour la notification
                AnchorPane notificationPane = createNotificationPane(reference, libelle, stock);

                // Ajouter la notification au panneau d'affichage des notifications
                notificationsPane.getChildren().add(notificationPane);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Mettre le VBox dans le ScrollPane
        notificationsScrollPane.setContent(notificationsPane);

        // Définir la taille du ScrollPane
        notificationsScrollPane.setFitToWidth(true);
        notificationsScrollPane.setFitToHeight(true);
    }

    // Méthode pour créer un panneau de notification avec des coins arrondis
    private AnchorPane createNotificationPane(String reference, String libelle, int stock) {
        // Créer un label pour afficher le message de notification
        Label notificationLabel = new Label("Stock bas pour : " + reference + " - " + libelle + " (Stock : " + stock + ")");
        notificationLabel.setTextFill(Color.WHITE);

        // Créer un panneau pour la notification avec des coins arrondis
        AnchorPane notificationPane = new AnchorPane(notificationLabel);
        notificationPane.setPrefSize(200, 50);
        notificationPane.setStyle("-fx-background-color: " + getRandomColor() + "; -fx-border-color: black; -fx-border-width: 1px; -fx-background-radius: 10;");

        return notificationPane;
    }

    // Méthode pour générer une couleur aléatoire
    private static String getRandomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return String.format("#%02x%02x%02x", r, g, b);
    }
}
