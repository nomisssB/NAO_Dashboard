package main;
/*
FILE: main.java
USAGE: main-method...
 */
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    //Variables Declaration
    public static Stage loginWindow;

    @Override
    public void start(Stage stage) throws Exception{

        Parent login = FXMLLoader.load(getClass().getResource("../gui/scene_login.fxml"));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("NaoDashboard MSKM - CONNECT");
        final Scene scene = new Scene(login);
        scene.setFill(null);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        loginWindow = stage;

    }


    public static void main(String[] args) {
      launch(args);
   }
}
