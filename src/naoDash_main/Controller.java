package naoDash_main;

import GUI.InputParse;
import NAO.NAO;
import javafx.collections.FXCollections;
import javafx.collections.FXCollections.*;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Controller {

    private double motionspeed = 0;
    private Color color;
    private String robotURL;
    private NAO nao1;

    @FXML
    public AnchorPane pane_main;
    public AnchorPane pane_control;
    public Slider sldr_speed;
    public Button btn_s;
    public Button btn_a;
    public Button btn_d;
    public Button btn_w;
    public Button btn_connect;
    public Button btn_disconnect;
    public Button btn_execute;
    public Button btn_sayText;
    public ColorPicker col_picker;
    public ListView motion_list;
    public Pane pane_cam;
    public TextField txt_ipadress;
    public TextField txt_port;
    public TextField txt_sayText;
    public Button btn_right;
    public Button btn_left;
    public Button btn_up;
    public Button btn_down;
    public Label lbl_toolbar;


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
//            logger.info("value Slider changed to:" + newValue.intValue());
            lbl_toolbar.setText("value: " + newValue.intValue());
            motionspeed = newValue.intValue();

        });



        //Abfangen von KeyEvents
        pane_main.setOnKeyPressed(e ->{
            switch (e.getCode()){
                case W: logger.info("Button W");btn_w.fire(); break;
                case A: logger.info("Button A");btn_a.fire(); break;
                case S: logger.info("Button S");btn_s.fire(); break;
                case D: logger.info("Button D");btn_d.fire(); break;
                case I: logger.info("Button I");btn_up.fire(); break;
                case K: logger.info("Button K");btn_down.fire(); break;
                case J: logger.info("Button J");btn_left.fire(); break;
                case L: logger.info("Button L");btn_right.fire(); break;
            }
        });

        //Füllen der ListView mit einer ArrayList
        ObservableList<String> items = FXCollections.observableArrayList(
                "Crouch","Sit down","stand up","dance","shutDown");
        motion_list.setItems(items);
        //Nur Auswählen eines Eintrages möglich:
         motion_list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }


    //#####################  CONNECTION ##################
    //Button Connect

    public void connect(ActionEvent actionEvent) {

        InputParse parser = new InputParse();
        String warning="";

        //folgendes ist noch vereinfachbar

        if(!parser.validateIP(txt_ipadress.getText()) || !parser.validatePort(txt_port.getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong Input");
            alert.setHeaderText("Please check your Input");
            if(txt_ipadress.getText().isEmpty() == true){
                warning = "Please type in an IP address!";
            }
            else if (!parser.validateIP(txt_ipadress.getText())) {
                warning = txt_ipadress.getText() + " is not a valid IP address!";
            }
            if(txt_port.getText().isEmpty() == true){
                warning = warning + "\n" + "Please type in a port number!";
            }
            else if (!parser.validatePort(txt_port.getText())){
                warning = warning + "\n" + txt_port.getText() + " is not a valid port number!";
            }
            alert.setContentText(warning);
            alert.showAndWait();
        } else {
            lbl_toolbar.setText("connect");
            robotURL = "tcp://" + txt_ipadress.getText() + ":" + txt_port.getText();
//            nao1 = new NAO();
//            nao1.establishConnection(robotURL);
            pane_control.setDisable(false);
            btn_connect.setDisable(true);
            btn_disconnect.setDisable(false);
        }
    }

    public void disconnect(ActionEvent actionEvent) {
        pane_control.setDisable(true);
        btn_connect.setDisable(false);
        btn_disconnect.setDisable(true);
    }

    //#####################  HEAD-CONTROL ##################
    //Buttons IJKL for Head-Control

    public void head_up(ActionEvent actionEvent) throws Exception{
        nao1.moveHead("up");
    }

    public void head_down(ActionEvent actionEvent) throws Exception{
        nao1.moveHead("down");
    }

    public void head_left(ActionEvent actionEvent) throws Exception{
        nao1.moveHead("left");
    }

    public void head_right(ActionEvent actionEvent) throws Exception{
        nao1.moveHead("right");
    }

    //#####################  BODY-CONTROL ##################
    // Buttons WASD for Body-Control

    public void forward(ActionEvent actionEvent){
        lbl_toolbar.setText("forward");
    }

    public void left(ActionEvent actionEvent) {
        lbl_toolbar.setText("left");
    }

    public void backward(ActionEvent actionEvent) {
        lbl_toolbar.setText("backward");
    }

    public void right(ActionEvent actionEvent) {
        lbl_toolbar.setText("right");
    }

    //#####################  SAY-TEXT ##################
    // Buttons WASD for Body-Control

    public void sayText(ActionEvent actionEvent) throws Exception{
        String TextToSay = txt_sayText.getText();
        nao1.sayText(TextToSay);
    }

    //#####################  LED-CONTROL ##################
    //After picking a color in ColorPicker
    public void colorchoice(ActionEvent actionEvent) {
        color = col_picker.getValue();
        lbl_toolbar.setText(color.toString());
    }



    //#####################  MENU-BAR ##################
    public void menu_quit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void menu_help(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About...");
        alert.setHeaderText("NAO Dashboard V1.0 Beta");
        alert.setContentText("By Simon Bienroth, Mustafa Mado, Khaled Jebrini\n and Michael Bachmann");

        alert.showAndWait();

    }



    public void p_motion(ActionEvent actionEvent) {
       String motion =  motion_list.getSelectionModel().getSelectedItem().toString();
        lbl_toolbar.setText("execute " + motion);
    }



}


