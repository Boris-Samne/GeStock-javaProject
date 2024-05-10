import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root;
        try {
            root= FXMLLoader.load(getClass().getResource("Fxml/Login.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("Css/style.css");

            // primaryStage.setMinHeight(600);
            // primaryStage.setMinWidth(860);

            //changer l'icone de l'application
            Image icone= new Image("images/G.png");
            primaryStage.getIcons().add(icone);

            //changer le titre de l'application
            primaryStage.setTitle("Gestion de STock");

            //mettre en place la scene.
            primaryStage.setScene(scene);

            //montrer la Scene principale.
            primaryStage.show();
     
        } catch (Exception e) {
            System.out.println("il y a des erreurs "+ e.getMessage());
        }
    }
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
