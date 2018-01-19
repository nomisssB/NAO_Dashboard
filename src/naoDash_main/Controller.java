package naoDash_main;

import GUI.Configurator;
import GUI.InputParse;
import NAO.ConnectionException;
import NAO.NAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.List;



public class Controller {

    private float motionspeed = 0.5f;
    private float volume = 0.5f;
    private float pitch = 0f;
    private Color color;
    private String robotURL;
    private NAO nao1;
    private String configFile = "config.xml";
    public static Stage prefs;
    private int batteryV;                               //  BatteryValue;

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
    public ListView<String> motion_list;
    public Pane pane_cam;
    public TextField txt_ipadress;
    public TextField txt_port;
    public TextField txt_sayText;
    public Button btn_right;
    public Button btn_left;
    public Button btn_up;
    public Button btn_down;
    public Label lbl_toolbar;
    public Slider sldr_pitch;
    public Slider sldr_volume;
    public ChoiceBox cb_voice;
    public CheckBox chb_pitch;
    public ProgressBar battery_bar;







    //KONSTRUKTOR
    public Controller(){

        //Führt Methode "saveConfig" bei Schließen des Programms aus
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
               saveConfig();
            }
        }));

    }

    @FXML
    public void initialize() throws IOException {


//        Abfangen von Werten des Sliders
        sldr_speed.valueProperty().addListener((observable, oldValue, newValue) -> {
            lbl_toolbar.setText("value: " + newValue.floatValue());
            motionspeed = newValue.floatValue();
            try {
                nao1.setMoveV(motionspeed);
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
        });

        sldr_pitch.valueProperty().addListener((observable, oldValue, newValue) -> {
            lbl_toolbar.setText("value: " + newValue.floatValue());
            pitch = newValue.floatValue();

        });

        sldr_volume.valueProperty().addListener((observable, oldValue, newValue) -> {
            lbl_toolbar.setText("value: " + newValue.floatValue());
            volume = newValue.floatValue();
        });

        chb_pitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
           if(chb_pitch.isSelected()){
               sldr_pitch.setDisable(false);
           } else { sldr_pitch.setDisable(true);}
        });



        //Abfangen von KeyEvents und Auslösen der Buttons je nach Key
        pane_main.setOnKeyPressed(e ->{
            switch (e.getCode()){
                case W: btn_w.fire(); break;
                case A: btn_a.fire(); break;
                case S: btn_s.fire(); break;
                case D: btn_d.fire(); break;
                case I: btn_up.fire(); break;
                case K: btn_down.fire(); break;
                case J: btn_left.fire(); break;
                case L: btn_right.fire(); break;
            }
        });

        pane_main.setOnKeyReleased(e ->{
            switch(e.getCode()){
                case W:
                    try {
                        nao1.setMoveX(0f);
                    } catch (ConnectionException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case A:
                    try {
                        nao1.setMoveY(0f);
                    } catch (ConnectionException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case S:
                    try {
                        nao1.setMoveX(0f);
                    } catch (ConnectionException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case D:
                    try {
                        nao1.setMoveY(0f);
                    } catch (ConnectionException e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        });


        //Laden der Einstellungen aus XML-Config-Datei
        Configurator.loader(configFile);
            //Übernehmen der geladenen Werte in Text-Felder
            txt_ipadress.setText(Configurator.props.getProperty("ipAddress"));
            txt_port.setText(Configurator.props.getProperty("port"));
//            sldr_volume.setValue(Float.parseFloat(Configurator.props.getProperty("volume")));


        // zweites Fenster für Einstellungen:
        try {
            Parent prefsParent = FXMLLoader.load(getClass().getResource("../GUI/preferences.fxml"));
            prefs = new Stage();
            prefs.setScene(new Scene(prefsParent));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //#####################  CONNECTION ##################
    //Button Connect
    public void connect(ActionEvent actionEvent) throws Exception {
        //neue Instanz von InputParse
        InputParse parser = new InputParse();
        //Variable warning für Ausgabe der Fehlermeldung
        String warning="";

        //Erzeugen der Warnmeldung, falls Eingaben nicht in RegEx passen
        if(!parser.validateIP(txt_ipadress.getText()) || !parser.validatePort(txt_port.getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong Input");
            alert.setHeaderText("Please check your Input");
            //Feld IP-Adresse leer:
            if(txt_ipadress.getText().isEmpty()){
                warning = "Please type in an IP address!";
            } //Feld IP-Adresse falsche Eingabe:
            else if (!parser.validateIP(txt_ipadress.getText())) {
                warning = txt_ipadress.getText() + " is not a valid IP address!";
            } //Feld Port leer:
            if(txt_port.getText().isEmpty()){
                warning = warning + "\n" + "Please type in a port number!";
            } //Feld Port falsche Eingabe:
            else if (!parser.validatePort(txt_port.getText())){
                warning = warning + "\n" + txt_port.getText() + " is not a valid port number!";
            } //Setzen der Warnmeldung und Anzeigen des Fehler-Dialogs
            alert.setContentText(warning);
            alert.showAndWait();
        } else { //Falls Eingaben korrekt, Connection öffnen:
            lbl_toolbar.setText("connect");
            robotURL = "tcp://" + txt_ipadress.getText() + ":" + txt_port.getText();
            nao1 = new NAO();
            nao1.establishConnection(robotURL);
            //Sperren/Entsperren der entsprechenden Kontroll-Objekte
            pane_control.setDisable(false);
            btn_connect.setDisable(true);
            btn_disconnect.setDisable(false);
            //Füllen der ListView mit den Postures des Naos
            fillPostureList(nao1.getPostures());
            fillVoiceList(nao1.getVoices());

        }


    }

    public void disconnect(ActionEvent actionEvent) {
        nao1.closeConnection();
        pane_control.setDisable(true);
        btn_connect.setDisable(false);
        btn_disconnect.setDisable(true);
    }

    private void fillPostureList(List<String> inputList){
        ObservableList<String> insert = FXCollections.observableArrayList(inputList);
        motion_list.setItems(insert);
    }

    private void fillVoiceList(List<String> inputList){
        ObservableList<String> insert = FXCollections.observableArrayList(inputList);
        cb_voice.setItems(insert);
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
        try {
            nao1.setMoveX(1f);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }

    }

    public void left(ActionEvent actionEvent) {
        lbl_toolbar.setText("left");
        try {
            nao1.setMoveY(1f);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    public void backward(ActionEvent actionEvent) {
        lbl_toolbar.setText("backward");
        try {
            nao1.setMoveX(-1f);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    public void right(ActionEvent actionEvent) {
        lbl_toolbar.setText("right");
        try {
            nao1.setMoveY(-1f);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    //#####################  SAY-TEXT ##################
    // Buttons WASD for Body-Control

    public void sayText(ActionEvent actionEvent) throws Exception{
        String TextToSay = txt_sayText.getText();
        nao1.sayText(TextToSay,cb_voice.getSelectionModel().getSelectedItem().toString(),volume,pitch);
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
       String motion = motion_list.getSelectionModel().getSelectedItem();
        try {
            nao1.execPosture(motion);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        lbl_toolbar.setText("execute " + motion);
    }

    private void saveConfig(){
        Configurator.saver(configFile,"ipAddress",txt_ipadress.getText());
        Configurator.saver(configFile,"port",txt_port.getText());
        Configurator.saver(configFile,"pitch",Float.toString(pitch));
    }

    public void batteryView () {
        try {
            batteryV =  nao1.batteryPercent();
            battery_bar.setProgress(batteryV);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void menu_prefs(ActionEvent actionEvent) {
        prefs.show();
    }
}


