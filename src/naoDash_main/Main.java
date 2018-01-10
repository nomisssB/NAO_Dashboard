package naoDash_main;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{

        //Erzeugung der Main-Scene "root" aus main_scene.fxml

        Parent root = FXMLLoader.load(getClass().getResource("../GUI/main_scene.fxml"));
        primaryStage.setTitle("NaoDashboard MSKM");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
