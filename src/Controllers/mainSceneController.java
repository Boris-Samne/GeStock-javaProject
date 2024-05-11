package Controllers;


import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
// import javafx.scene.layout.VBox;

public class mainSceneController {

    @FXML
    private TextField tfTitle;

    @FXML
    private ImageView imdF;


    @FXML
    private Pane pan1;

    @FXML
    private StackPane contentArea;

    // @FXML
    // private VBox contentArea;


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
