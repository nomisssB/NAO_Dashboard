package naoDash_main;

import javafx.collections.FXCollections;
import javafx.collections.FXCollections.*;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


public class Controller {

    public double motionspeed = 0;
    public Color color;

    @FXML
    public VBox vbox_main;
    public Slider sldr_speed;
    public Button btn_s;
    public Button btn_a;
    public Button btn_d;
    public Button btn_w;
    public Button btn_connect;
    public Button btn_execute;
    public Label lbl_mid;
    public ColorPicker col_picker;
    public ListView motion_list;
    public Pane pane_cam;


    public Controller(){


    }

    @FXML
    public void initialize() throws IOException {
        //Logger um Events und Fehler zu dokumentieren. Log Datei unter root\main_log.log (XML)
        FileHandler handler = new FileHandler("main_log.log", true);
        Logger logger = Logger.getLogger("NAODash Logger");
        logger.addHandler(handler);

//        Abfangen von Werten des Sliders
        sldr_speed.valueProperty().addListener((observable, oldValue, newValue) -> {
            logger.info("value Slider changed to:" + newValue.intValue());
            lbl_mid.setText("value: " + newValue.intValue());
            motionspeed = newValue.intValue();

        });



        //Abfangen von KeyEvents
        vbox_main.setOnKeyPressed(e ->{
                switch (e.getCode()){
                case W: logger.info("Button W");btn_w.fire(); break;
                case A: logger.info("Button A");btn_a.fire(); break;
                case S: logger.info("Button S");btn_s.fire(); break;
                case D: logger.info("Button D");btn_d.fire(); break;
            }
        });

        //Füllen der ListView mit einer ArrayList
        ObservableList<String> items = FXCollections.observableArrayList(
                "Crouch","Sit down","stand up","dance","shutDown");
        motion_list.setItems(items);
        //Nur Auswählen eines Eintrages möglich:
         motion_list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }



    public void forward(ActionEvent actionEvent){
        lbl_mid.setText("forward");

    }

    public void left(ActionEvent actionEvent) {
        lbl_mid.setText("left");

    }

    public void backward(ActionEvent actionEvent) {
        lbl_mid.setText("backward");
    }

    public void right(ActionEvent actionEvent) {
        lbl_mid.setText("right");
    }
    public void connect(ActionEvent actionEvent) {
        lbl_mid.setText("connect");

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Connection failed!");
        alert.setContentText("Try again");

        alert.showAndWait();

    }

    public void colorchoice(ActionEvent actionEvent) {
        color = col_picker.getValue();
        lbl_mid.setText(color.toString());
    }

    public void menu_quit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void menu_help(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About...");
        alert.setHeaderText("NAO Dashboard V1.0 Beta");
        alert.setContentText("By Simon Bienroth, Mustafa Mado, Khaled Jebrini\n and Michael Bachmann");

        alert.showAndWait();
//testComment
    }

    public void p_motion(ActionEvent actionEvent) {
       String motion =  motion_list.getSelectionModel().getSelectedItem().toString();
        lbl_mid.setText("execute " + motion);
    }
}


