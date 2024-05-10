package Controllers;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class loginController implements Initializable{

  
    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField username;

    @FXML
    private Button loginBtn;
    @FXML
    private Label isConnected;

    @FXML
    private PasswordField password;
    //    DATABASE TOOLS
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private double x = 0;
    private double y = 0;
    @FXML
    void loginAdmin(ActionEvent event) throws SQLException {
        String sql = "SELECT * FROM admin WHERE username = ? and password = ?";
        connect = DB.getConnection();
        
        try{
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());
            
            result = prepare.executeQuery();
            Alert alert;
            
            if(username.getText().isEmpty() || password.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                if(result.next()){
                   // getData.username = username.getText();
                  //  Stage primaryStage
                //  PrimaryStage primaryStage=new PrimaryStage
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login");
                    alert.showAndWait();
                    loginBtn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("/Fxml/mainScene.fxml"));
                   
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                   // scene.getStylesheets().add("/Css/style.css");
                    stage.setMinHeight(600);
                    stage.setMinWidth(860);
                    
                   Image icone= new Image("/images/test.jpg");
                   stage.getIcons().add(icone);

            //changer le titre de l'application
                   stage.setTitle("Gestion de STock");
                   stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                    
                }else{
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username/Password");
                    alert.showAndWait();

                }
            }
            
        }catch(Exception e){e.printStackTrace();}
    }

   
    // @FXML
    // void btnOkClicked(ActionEvent event) throws IOException {
    //     Parent root;
    //     root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
    //     Stage stage =new Stage();
    //         Scene scene = new Scene(root);
            
    //         stage.setScene(scene);
    //         stage.show();
    // }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

}
