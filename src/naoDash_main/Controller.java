package naoDash_main;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.ToggleSwitch;

import java.io.IOException;
import java.util.List;

import static naoDash_main.Main.loginWindow;
import static GUI.LoginController.nao1;
import static GUI.LoginController.rootWindow;


public class Controller {


    private float motionspeed = 0.5f;
    private float volume = 0.5f;
    private float pitch = 0f;
    private Color color;
    private Timeline batteryTimeline;
    public static Stage prefs;
    private int armModeJoint;
    private int armModeSide;
    private String[][] armControl1, armControl2;

    @FXML
    public ProgressBar battery_Bar;
    public AnchorPane pane_main;
    public AnchorPane pane_control;
    public AnchorPane pane_sounds;
    public Slider sldr_speed;
    public Button btn_s;
    public Button btn_a;
    public Button btn_d;
    public Button btn_w;
    public Button btn_q;
    public Button btn_e;
    public Button btn_i;
    public Button btn_j;
    public Button btn_k;
    public Button btn_l;
    public Button btn_disconnect;
    public Button btn_execute;
    public Button btn_sayText;
    public Button btn_playSound;
    public ColorPicker col_picker_left;
    public ColorPicker col_picker_right;
    public ListView<String> motion_list;
    public ListView<String> sound_list;
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
    public ToggleSwitch ts_shoulder;
    public ToggleSwitch ts_elbow;
    public ToggleSwitch ts_hand;
    public Circle highTemp;
    public Circle midTemp;
    public Circle lowTemp;

    //KONSTRUKTOR
    public Controller() {

        // Arrays to determine the right joint for the arm control.
        // first digit is for left/right // second digit for wrist/elbow/shoulder
        armControl1 = new String[2][3];
        armControl2 = new String[2][3];
        armControl1[0][0] = "LShoulderPitch";
        armControl2[0][0] = "LShoulderRoll";
        armControl1[0][1] = "LElbowYaw";
        armControl2[0][1] = "LElbowRoll";
        armControl1[0][2] = "LWristYaw";
        armControl2[0][2] = "LHand";
        armControl1[1][0] = "RShoulderPitch";
        armControl2[1][0] = "RShoulderRoll";
        armControl1[1][1] = "RElbowYaw";
        armControl2[1][1] = "RElbowRoll";
        armControl1[1][2] = "RWristYaw";
        armControl2[1][2] = "RHand";


/*        //Führt Methode "saveConfig" bei Schließen des Programms (des Threads) aus TODO derzeit nicht in Benutzung
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                saveConfig();
            }
        }));*/

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
            if (chb_pitch.isSelected()) {
                sldr_pitch.setDisable(false);
            } else {
                sldr_pitch.setDisable(true);
                pitch = 0.0f;
            }
        });


        //Abfangen von KeyEvents und Auslösen der Buttons je nach Key
        pane_main.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                    btn_w.fire();
                    break;
                case A:
                    btn_a.fire();
                    break;
                case S:
                    btn_s.fire();
                    break;
                case D:
                    btn_d.fire();
                    break;
                case Q:
                    btn_q.fire();
                    break;
                case E:
                    btn_e.fire();
                    break;
                case I:
                    btn_i.fire();
                    break;
                case K:
                    btn_k.fire();
                    break;
                case J:
                    btn_j.fire();
                    break;
                case L:
                    btn_l.fire();
                    break;
            }
        });

        pane_main.setOnKeyReleased(e -> {
            switch (e.getCode()) {
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



    try {
            fillPostureList(nao1.getPostures());
            fillVoiceList(nao1.getVoices());
            fillSoundList(nao1.getSoundFiles());
            nao1.setMoveV(motionspeed);

            //initalisiert Battery-ProgressBar und startet "Timeline" für die Batterie-Anzeige
            battery_Bar.setProgress(nao1.batteryPercent());
            batteryViewer();
        } catch (ConnectionException | InterruptedException e) {
            e.printStackTrace();
        }
        //initalisiert Temp-Ampel Anzeige


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
    public void disconnect(ActionEvent actionEvent) {
        nao1.closeConnection();
        batteryTimeline.stop();
        rootWindow.hide();
        loginWindow.show();
    }

    private void fillPostureList(List<String> inputList) {
        ObservableList<String> insert = FXCollections.observableArrayList(inputList);
        motion_list.setItems(insert);
    }

    private void fillVoiceList(List<String> inputList) {
        ObservableList<String> insert = FXCollections.observableArrayList(inputList);
        cb_voice.setItems(insert);
    }

    private void fillSoundList(List<String> inputList) throws ConnectionException {
        if (nao1.getSoundFiles()==null){
            pane_sounds.setVisible(false);
            return;
        }
            ObservableList<String> insert = FXCollections.observableArrayList(inputList);
            sound_list.setItems(insert);
    }


    //#####################  HEAD-CONTROL ##################
    //Buttons IJKL for Head-Control

    public void head_up(ActionEvent actionEvent) throws Exception {
        nao1.moveHead("up");
    }

    public void head_down(ActionEvent actionEvent) throws Exception {
        nao1.moveHead("down");
    }

    public void head_left(ActionEvent actionEvent) throws Exception {
        nao1.moveHead("left");
    }

    public void head_right(ActionEvent actionEvent) throws Exception {
        nao1.moveHead("right");
    }

    //#####################  BODY-CONTROL ##################
    // Buttons WASD for Body-Control

    public void forward(ActionEvent actionEvent) {
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

    //#####################  SAY-TEXT #####################
    public void sayText(ActionEvent actionEvent) throws Exception {
        String TextToSay = txt_sayText.getText();
        nao1.sayText(TextToSay, cb_voice.getSelectionModel().getSelectedItem().toString(), volume, pitch);
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
    public void p_sound(ActionEvent actionEvent) throws ConnectionException {

        String sound = sound_list.getSelectionModel().getSelectedItem();


           try {
               nao1.playSound(sound);
           } catch (ConnectionException e) {
               e.printStackTrace();
           }

        lbl_toolbar.setText("play " + sound);
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


    public void setbatteryView() {
        try {
            battery_Bar.setProgress(nao1.batteryPercent());
            System.out.println(nao1.batteryPercent());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void batteryViewer() {
        batteryTimeline = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> setbatteryView()));
        batteryTimeline.setCycleCount(Animation.INDEFINITE);
        batteryTimeline.play();
    }

    public void menu_prefs(ActionEvent actionEvent) {
        prefs.show();
    }

    //#####################  ARM-CONTROL ##################
    //Arrow-Buttons for Arm-Control

    public void arm_up(ActionEvent actionEvent) throws Exception {
        nao1.moveArm(armControl1[armModeJoint][armModeSide],-1);
    }

    public void arm_down(ActionEvent actionEvent) throws Exception {
        nao1.moveArm(armControl1[armModeJoint][armModeSide],1);
    }

    public void arm_left(ActionEvent actionEvent) throws Exception {
        nao1.moveArm(armControl2[armModeJoint][armModeSide],-1);
    }

    public void arm_right(ActionEvent actionEvent) throws Exception {
        nao1.moveArm(armControl2[armModeJoint][armModeSide],1);
    }

    public void handSelect(MouseEvent mouseEvent) {
        if (ts_hand.isSelected()) {
            ts_shoulder.setSelected(false);
            ts_elbow.setSelected(false);
            armModeJoint = 2;
        }
    }

    public void shoulderSelect(MouseEvent mouseEvent) {
        if (ts_shoulder.isSelected()) {
            ts_hand.setSelected(false);
            ts_elbow.setSelected(false);
            armModeJoint = 0;
        }
    }

    public void elbowSelect(MouseEvent mouseEvent) {
        if (ts_elbow.isSelected()) {
            ts_hand.setSelected(false);
            ts_shoulder.setSelected(false);
            armModeJoint = 1;
        }
    }


}


