package naoDash_main;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public static Stage mainFrame;


    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent login = FXMLLoader.load(getClass().getResource("../GUI/login.fxml"));
        primaryStage.setTitle("NaoDashboard MSKM - LOGIN");
        primaryStage.setScene(new Scene(login));
        primaryStage.setResizable(false);
        primaryStage.show();


        //Erzeugung der Main-Scene "root" aus main_scene.fxml

//        Parent root = FXMLLoader.load(getClass().getResource("../GUI/main_scene.fxml"));
//        primaryStage.setTitle("NaoDashboard MSKM");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.setResizable(false);
//        primaryStage.show();

        mainFrame = primaryStage;
    }


    public static void main(String[] args) {

        launch(args);


    }
}
