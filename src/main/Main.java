package main;
/*
* FILE: main.java
* USAGE: main-method...
*/
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    // Variables Declaration
    public static Stage loginWindow;

    @Override
    public void start(Stage stage) throws Exception{
        // creates the stage for login-window
        Parent login = FXMLLoader.load(getClass().getResource("../gui/scene_login.fxml"));
        stage.setTitle("NaoDashboard MSKM - CONNECT"); // Name for program
        final Scene scene = new Scene(login);
        stage.initStyle(StageStyle.TRANSPARENT); // transparent background!
        scene.setFill(null);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("../pic/icon.png"))); // Window-Icon
        stage.setResizable(false); // static window size
        stage.show();
        loginWindow = stage;
    }

    public static void main(String[] args) {
      launch(args);
   }
}
