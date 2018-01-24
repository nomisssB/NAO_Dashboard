package naoDash_main;

import GUI.Configurator;
import GUI.InputParse;
import GUI.Timers;
import NAO.ConnectionException;
import NAO.NAO;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

import static GUI.Configurator.configFile;
import static GUI.LoginController.nao1;


public class Controller {


    private float motionspeed = 0.5f;
    private float volume = 0.5f;
    private float pitch = 0f;
    private Color color;
    private Timeline batteryTimeline;
    public static Stage prefs;

    @FXML
    public ProgressBar battery_Bar;
    public ProgressBar temp_Bar;
    public AnchorPane pane_main;
    public AnchorPane pane_control;
    public Slider sldr_speed;
    public Button btn_s;
    public Button btn_a;
    public Button btn_d;
    public Button btn_w;
    public Button btn_q;
    public Button btn_e;
    public Button btn_connect;
    public Button btn_disconnect;
    public Button btn_execute;
    public Button btn_sayText;
    public ColorPicker col_picker_left;
    public ColorPicker col_picker_right;
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
    public Button btn_play;

    //KONSTRUKTOR
    public Controller(){

        //Führt Methode "saveConfig" bei Schließen des Programms (des Threads) aus
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
               saveConfig();
            }
        }));

    }

    @FXML
    public void initialize() throws IOException {


//        Listener for sliders
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

        // Listener for Checkbox to enable/disable "Pitch"-Slider
        chb_pitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
           if(chb_pitch.isSelected()){
               sldr_pitch.setDisable(false);
           } else { sldr_pitch.setDisable(true); pitch = 0.0f;}
        });


        //Abfangen von KeyEvents und Auslösen der Buttons je nach Key
        pane_main.setOnKeyPressed(e ->{
            switch (e.getCode()){
                case W: btn_w.fire(); break;
                case A: btn_a.fire(); break;
                case S: btn_s.fire(); break;
                case D: btn_d.fire(); break;
                case Q: btn_q.fire(); break;
                case E: btn_e.fire(); break;
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
                case Q:
                    try {
                        nao1.setMoveT(0f);
                    } catch (ConnectionException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case E:
                    try {
                        nao1.setMoveT(0f);
                    } catch (ConnectionException e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        });


        //Laden der Einstellungen aus XML-Config-Datei
//        Configurator.loader(configFile);
//            //Übernehmen der geladenen Werte in Text-Felder
//            txt_ipadress.setText(Configurator.props.getProperty("ipAddress"));
//            txt_port.setText(Configurator.props.getProperty("port"));
//            sldr_volume.setValue(Float.parseFloat(Configurator.props.getProperty("volume")));


        try {
            fillPostureList(nao1.getPostures());
            fillVoiceList(nao1.getVoices());
            nao1.setMoveV(motionspeed);

            //initalisiert Battery-ProgressBar und startet "Timeline" für die Batterie-Anzeige
            battery_Bar.setProgress(nao1.batteryPercent());
            batteryViewer();
        } catch (ConnectionException | InterruptedException e) {
            e.printStackTrace();
        }


        // zweites Fenster für Einstellungen: (noch nicht in Benutzung)
        try {
            Parent prefsParent = FXMLLoader.load(getClass().getResource("../GUI/preferences.fxml"));
            prefs = new Stage();
            prefs.setScene(new Scene(prefsParent));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //#####################  CONNECTION ##################
    public void connect(ActionEvent actionEvent) throws Exception {

    }


    public void disconnect(ActionEvent actionEvent) {
        nao1.closeConnection();
        pane_control.setDisable(true);
        btn_connect.setDisable(false);
        btn_disconnect.setDisable(true);
        batteryTimeline.stop();

    }

    protected void fillPostureList(List<String> inputList){
        ObservableList<String> insert = FXCollections.observableArrayList(inputList);
        motion_list.setItems(insert);
    }

    protected void fillVoiceList(List<String> inputList){
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

    public void turnRight(ActionEvent actionEvent) {
        try {
            nao1.setMoveT(-1f);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    public void turnLeft(ActionEvent actionEvent) {
        try {
            nao1.setMoveT(1f);
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
    public void colorchoice_left(ActionEvent actionEvent) {
        color = col_picker_left.getValue();
        lbl_toolbar.setText(color.toString());
        try {
            nao1.changeEyeColor("Left", color);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }
    //After picking a color in ColorPicker
    public void colorchoice_right(ActionEvent actionEvent) {
        color = col_picker_right.getValue();
        lbl_toolbar.setText(color.toString());
        try {
            nao1.changeEyeColor("Right", color);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
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

    //  Sounds list
    /*
     public void p_sound(ActionEvent actionEvent) {
        String sound = sound_list.getSelectionModel().getSelectedItem();
        try {
            nao1.getSoundFiles(sound);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        lbl_toolbar.setText("play " + sound);
    }

    */


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
//        Configurator.saver(configFile,"volume",Double.toString(volume));
//        Configurator.saver(configFile,"pitch",Float.toString(pitch));
    }

    public void setbatteryView () {
        try {
            battery_Bar.setProgress(nao1.batteryPercent());
            System.out.println(nao1.batteryPercent());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void batteryViewer(){
        batteryTimeline = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> setbatteryView()));
        batteryTimeline.setCycleCount(Animation.INDEFINITE);
        batteryTimeline.play();
    }

    public void menu_prefs(ActionEvent actionEvent) {
        prefs.show();
    }


}


