package Controllers;


import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
// import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class mainSceneController {
    
    @FXML
    private Button close;

    @FXML
    private Button min;

    @FXML
    private TextField tfTitle;

    @FXML
    private ImageView imdF;


    @FXML
    private Pane pan1;
    @FXML
    private BorderPane main_form;
    @FXML
    private Button logout;
    @FXML
    private StackPane contentArea;

    // @FXML
    // private VBox contentArea;
   @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    void logout(ActionEvent event) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {

                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                // root.setOnMousePressed((MouseEvent event) -> {
                //     x = event.getSceneX();
                //     y = event.getSceneY();
                // });

                // root.setOnMouseDragged((MouseEvent event) -> {
                //     stage.setX(event.getScreenX() - x);
                //     stage.setY(event.getScreenY() - y);

                //     stage.setOpacity(.8);
                // });

                // root.setOnMouseReleased((MouseEvent event) -> {
                //     stage.setOpacity(1);
                // });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void btnArticlesClicked(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/Fxml/Article.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml); 
    }

    @FXML
    void btnClientsClicked(ActionEvent event) throws Exception {
        Parent fxml = FXMLLoader.load(getClass().getResource("/Fxml/Client.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    @FXML
    void btnFournisseursClicked(ActionEvent event) throws Exception {
        Parent fxml = FXMLLoader.load(getClass().getResource("/Fxml/fourns.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    @FXML
    void btnVentesClicked(ActionEvent event) throws Exception {
        Parent fxml = FXMLLoader.load(getClass().getResource("/Fxml/ventes.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);

    }

    @FXML
    void btnAchatClicked(ActionEvent event) throws Exception{
        Parent fxml = FXMLLoader.load(getClass().getResource("/Fxml/achat.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    @FXML
    void btnNotificationClicked(ActionEvent event) throws Exception{
        Parent fxml = FXMLLoader.load(getClass().getResource("/Fxml/notification.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

}
