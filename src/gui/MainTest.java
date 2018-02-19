package gui;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainTest extends Application {

    public static Stage loginWindow;

    @Override
    public void start(Stage primaryStage) throws Exception{

//        Parent login = FXMLLoader.load(getClass().getResource("../GUI/login.fxml"));
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        primaryStage.setTitle("NaoDashboard MSKM - LOGIN");
//        final Scene scene = new Scene(login);
//        scene.setFill(null);
//        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
//        primaryStage.show();
//        loginWindow = primaryStage;


        //Erzeugung der Main-Scene "root" aus main_scene.fxml

        Parent root = FXMLLoader.load(getClass().getResource("/main_scene.fxml"));
        primaryStage.setTitle("NaoDashboard MSKM");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();


    }


    public static void main(String[] args) {

        launch(args);


    }
}
