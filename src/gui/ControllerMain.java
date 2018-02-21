package gui;
/*
FILE: Controller.java
USAGE: Controller for Main-Dashboard-Window. Contains all Methods which are needed for GUI-control
 */
import nao.ConnectionException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.controlsfx.control.ToggleSwitch;

import java.io.IOException;
import java.util.List;

import static main.Main.loginWindow;
import static gui.ControllerLogin.nao1;
import static gui.ControllerLogin.rootWindow;


public class ControllerMain {

    // Variables declaration
    private float motionspeed = 0.5f;
    private float pitch = 0f;
    private Color color;
    private static Timeline batteryTimeline, tempTimeline, connectionCheckTimeline;
    private int armModeJoint = 0;
    private int armModeSide = 1;
    private String[][] armControl1, armControl2;

    //FXML Annotations
    @FXML
    private ProgressBar battery_Bar;
    @FXML
    private AnchorPane pane_main, pane_sounds;
    @FXML
    private Slider sldr_speed, sldr_pitch, sldr_volume;
    @FXML
    private Button btn_s, btn_a, btn_d, btn_w, btn_q, btn_e, btn_i, btn_j, btn_k, btn_l, btn_h, btn_f, btn_t, btn_g;
    @FXML
    private ColorPicker col_picker_left, col_picker_right;
    @FXML
    private ListView<String> motion_list, sound_list;
    @FXML
    private TextField txt_sayText, txt_tactileFront, txt_tactileMiddle, txt_tactileRear;
    @FXML
    private Label lbl_toolbar, lbl_battery;
    @FXML
    private ChoiceBox cb_voice;
    @FXML
    private CheckBox chb_pitch, chb_left, chb_right, chb_mirror_arm;
    @FXML
    private ToggleSwitch ts_shoulder, ts_elbow, ts_hand, ts_mirror_led, ts_camera, ts_rest, ts_armMovement;
    @FXML
    private Circle highTemp, midTemp, lowTemp;
    // ImageView must be public due to the special camera-thread
    public ImageView imageView11;

    //Constructor (Called first, then FXML Annotations, then "initialize"

    public ControllerMain() {
        // Arrays to determine the right joint for the arm control.
        // first digit is for left/right // second digit for wrist/elbow/shoulder
        // 1 -> left, 0-> right || 0 -> shoulder, 1 -> Elbow, 2 -> Hand/Wrist
        armControl1 = new String[2][3];
        armControl2 = new String[2][3];
        armControl1[1][0] = "LShoulderPitch";
        armControl2[1][0] = "LShoulderRoll";
        armControl1[1][1] = "LElbowRoll";
        armControl2[1][1] = "LElbowYaw";
        armControl1[1][2] = "LHand";
        armControl2[1][2] = "LWristYaw";
        armControl1[0][0] = "RShoulderPitch";
        armControl2[0][0] = "RShoulderRoll";
        armControl1[0][1] = "RElbowRoll";
        armControl2[0][1] = "RElbowYaw";
        armControl1[0][2] = "RHand";
        armControl2[0][2] = "RWristYaw";
    }

    @FXML
    public void initialize() throws IOException {
        // Listener for sliders
        sldr_speed.valueProperty().addListener((observable, oldValue, newValue) -> {
            lbl_toolbar.setText("value: " + newValue.floatValue());
            motionspeed = newValue.floatValue();
            try {
                nao1.setMoveV(motionspeed);
            } catch (ConnectionException e) {
                connectionLost();
            }
        });
        sldr_pitch.valueProperty().addListener((observable, oldValue, newValue) -> {
            lbl_toolbar.setText("value: " + newValue.floatValue());
            pitch = newValue.floatValue();
        });
        sldr_volume.valueProperty().addListener((observable, oldValue, newValue) -> {
            lbl_toolbar.setText("value: " + newValue.floatValue());
            try {
                nao1.setVolume(newValue.intValue());
            } catch (ConnectionException e) {
                connectionLost();
            }
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
        // Listener for TextFields
        txt_tactileFront.textProperty().addListener((observable, oldValue, newValue) -> {
            nao1.setTactileHeadTextFront(newValue);
        });
        txt_tactileMiddle.textProperty().addListener((observable, oldValue, newValue) -> {
            nao1.setTactileHeadTextMiddle(newValue);
        });
        txt_tactileRear.textProperty().addListener((observable, oldValue, newValue) -> {
            nao1.setTactileHeadTextRear(newValue);
        });


        //Listener for Key-Events (Press/Release)
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
                case T:
                    btn_t.fire();
                    break;
                case F:
                    btn_f.fire();
                    break;
                case H:
                    btn_h.fire();
                    break;
                case G:
                    btn_g.fire();
                    break;
            }
        });
        pane_main.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case W:
                    try {
                        nao1.setMoveX(0f);
                    } catch (ConnectionException e1) {
                        connectionLost();
                    }
                    break;
                case A:
                    try {
                        nao1.setMoveY(0f);
                    } catch (ConnectionException e1) {
                        connectionLost();
                    }
                    break;
                case S:
                    try {
                        nao1.setMoveX(0f);
                    } catch (ConnectionException e1) {
                        connectionLost();
                    }
                    break;
                case D:
                    try {
                        nao1.setMoveY(0f);
                    } catch (ConnectionException e1) {
                        connectionLost();
                    }
                    break;
                case Q:
                    try {
                        nao1.setMoveT(0f);
                    } catch (ConnectionException e1) {
                        connectionLost();
                    }
                    break;
                case E:
                    try {
                        nao1.setMoveT(0f);
                    } catch (ConnectionException e1) {
                        connectionLost();
                    }
                    break;
            }
        });

        nao1.initialize(imageView11);

        try {
            //fill ListView, Choicebox
            fillPostureList(nao1.getPostures());
            fillVoiceList(nao1.getVoices());
            fillSoundList(nao1.getSoundFiles());
            nao1.setMoveV(motionspeed);

            if(nao1.isInRestMode()){
                ts_rest.setSelected(false);
            } else {
                ts_rest.setSelected(true);
            }

            if(nao1.isCameraActivated()){
                ts_camera.setSelected(true);
            } else {
                ts_camera.setSelected(false);
            }

            if(nao1.isMoveArmsEnabled()){
                ts_armMovement.setSelected(true);
            } else {
                ts_armMovement.setSelected(false);
            }

            //initializes value for battery-ProgressBar and starts timeline for battery and temperature refresh
            battery_Bar.setProgress(nao1.getBatteryPercent());
            startConnectionCheck();
            batteryViewer();
            tempViewer();
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    // #####################  Connection Control ####################
    @FXML
    private void disconnect(ActionEvent actionEvent) {
        nao1.closeConnection();
        nao1 = null;
        batteryTimeline.stop();
        tempTimeline.stop();
        connectionCheckTimeline.stop();
        rootWindow.hide();
        loginWindow.show();
    }

    private void checkConnection(){
        if (!nao1.checkConnection(true)) {
            connectionLost();
        }
    }
    //What happens when NAO-connection is lost
    private static void connectionLost (){
        nao1.closeConnection();
        nao1 = null;
        batteryTimeline.stop();
        tempTimeline.stop();
        connectionCheckTimeline.stop();
        rootWindow.hide();
        loginWindow.show();
        Alert connectionAlert = new Alert(Alert.AlertType.WARNING);//When the connection couldn't be established
        connectionAlert.setTitle("Connection lost!");
        connectionAlert.setHeaderText("Something went wrong...");
        connectionAlert.setContentText("Connection has been lost. \nThis could be caused due to different reasons..." +
                "\ne.g. Network failed");
        connectionAlert.show();
    }

    // #####################  Fillers ####################
    private void fillPostureList(List<String> inputList) {
        ObservableList<String> insert = FXCollections.observableArrayList(inputList);
        motion_list.setItems(insert);
    }

    private void fillVoiceList(List<String> inputList) {
        ObservableList<String> insert = FXCollections.observableArrayList(inputList);
        cb_voice.setItems(insert);
        cb_voice.getSelectionModel().selectFirst();
    }

    private void fillSoundList(List<String> inputList) {
        try {
            if (nao1.getSoundFiles()==null){ // if there are no sound files, hide gui-module for sounds
                pane_sounds.setVisible(false);
                return;
            }
        } catch (ConnectionException e) {
            connectionLost();
        }
        ObservableList<String> insert = FXCollections.observableArrayList(inputList);
            sound_list.setItems(insert);
    }


    //#####################  HEAD-CONTROL ##################
    //Buttons IJKL for head control, call "moveHead"-method in Nao-Class with corresponding parameters

    public void head_up(ActionEvent actionEvent) {
        try {
            nao1.moveHead("up");
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    public void head_down(ActionEvent actionEvent) {
        try {
            nao1.moveHead("down");
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    public void head_left(ActionEvent actionEvent) {
        try {
            nao1.moveHead("left");
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    public void head_right(ActionEvent actionEvent) {
        try {
            nao1.moveHead("right");
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    //#####################  BODY-CONTROL ##################
    // Buttons WASD (direction) TE (angle) for body control, call corresponding "setMove_" in Nao-Class with direction (1f/-1f)

    public void forward(ActionEvent actionEvent) {
        lbl_toolbar.setText("forward");
        try {
            nao1.setMoveX(1f);
        } catch (ConnectionException e) {
            connectionLost();
        }

    }

    public void left(ActionEvent actionEvent) {
        lbl_toolbar.setText("left");
        try {
            nao1.setMoveY(1f);
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    public void backward(ActionEvent actionEvent) {
        lbl_toolbar.setText("backward");
        try {
            nao1.setMoveX(-1f);
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    public void right(ActionEvent actionEvent) {
        lbl_toolbar.setText("right");
        try {
            nao1.setMoveY(-1f);
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    public void turnRight(ActionEvent actionEvent) {
        try {
            nao1.setMoveT(-1f);
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    public void turnLeft(ActionEvent actionEvent) {
        try {
            nao1.setMoveT(1f);
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    public void stopWalk(ActionEvent actionEvent) {
        try {
            nao1.setMoveT(0f);
            nao1.setMoveX(0f);
            nao1.setMoveY(0f);
        } catch ( ConnectionException e) {
            connectionLost();
        }
    }

    // #####################  SAY-TEXT #####################
    // calls "sayText"-method in Nao-class with parameters "TextToSay" from textfield, "Voice" from choicebox and pitch effect value
    public void sayText(ActionEvent actionEvent)  {
        String TextToSay = txt_sayText.getText();
        try {
            nao1.sayText(TextToSay, cb_voice.getSelectionModel().getSelectedItem().toString(), pitch);
        } catch (ConnectionException e) {
            connectionLost();
        }
    }


    // #####################  LED-CONTROL ##################
    // After picking a color in ColorPicker
    // calls method "changeEyeColor" in Nao-class with corresponding parameters
    public void colorchoice_left(ActionEvent actionEvent) {
        color = col_picker_left.getValue(); // get Value from color-picker
        lbl_toolbar.setText(color.toString());
        try {
            nao1.changeEyeColor("Left", color);
            if(ts_mirror_led.isSelected()){
                nao1.changeEyeColor("Right",color);
                col_picker_right.setValue(color);
            }
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    public void colorchoice_right(ActionEvent actionEvent) {
        color = col_picker_right.getValue();
        lbl_toolbar.setText(color.toString());
        try {
            nao1.changeEyeColor("Right", color);
            if(ts_mirror_led.isSelected()){
                nao1.changeEyeColor("Left",color);
                col_picker_left.setValue(color);
            }
        } catch (ConnectionException e) {
            connectionLost();
        }
    }


    // #####################  MENU-BAR ##################
    public void menu_quit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void menu_help(ActionEvent actionEvent) {
        // opens information-dialogue
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About...");
        alert.setHeaderText("NAO Dashboard V1.0 Beta");
        alert.setContentText("By Simon Bienroth, Mustafa Mado, Khaled Jebrini\n and Michael Bachmann");

        alert.showAndWait();
    }

    // #####################  Executors for sound and postures ##################
    // calls methods in Nao-class with corresponding parameters
    public void p_sound(ActionEvent actionEvent) {
        String sound = sound_list.getSelectionModel().getSelectedItem();
           try {
               nao1.playSoundFile(sound);
           } catch (ConnectionException e) {
               connectionLost();
           }
        lbl_toolbar.setText("play " + sound);
       }

    public void p_motion(ActionEvent actionEvent) {
        String motion = motion_list.getSelectionModel().getSelectedItem();
        try {
            nao1.execPosture(motion);
        } catch (ConnectionException e) {
            connectionLost();
        }
        lbl_toolbar.setText("execute " + motion);
    }


    // Setter for GUI-elements
    private void setbatteryView() {
        try {
            boolean lowBat = nao1.getBatteryPercent() <= 20;
            boolean midBat = (nao1.getBatteryPercent() > 20) && (nao1.getBatteryPercent() < 50);
            if(lowBat) {
                battery_Bar.setStyle("-fx-progress-color: red");
            }else if (midBat){
                battery_Bar.setStyle("-fx-progress-color: yellow");
            }
            else{
                battery_Bar.setStyle("-fx-progress-color: lime");
            }
            battery_Bar.setProgress(nao1.getBatteryPercent());
            lbl_battery.setText(Double.toString(nao1.getBatteryPercent())+"%");
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    private void settempView()  {

        try {

            switch ((int) nao1.getTemp()) {
                case -1:
                    lowTemp.setOpacity(1.0);
                    midTemp.setOpacity(0.2);
                    highTemp.setOpacity(0.2);
                    break;
                case 0:
                    lowTemp.setOpacity(0.4);
                    midTemp.setOpacity(1.0);
                    highTemp.setOpacity(0.2);
                    break;
                case 1:
                    lowTemp.setOpacity(0.2);
                    midTemp.setOpacity(0.2);
                    highTemp.setOpacity(1.0);
                case -2:
                    midTemp.setOpacity(0.2);
                    midTemp.setOpacity(0.2);
                    midTemp.setOpacity(0.2);
            }
        } catch (ConnectionException e) {
            connectionLost();
        }

    }



    //timelines for battery and temperature refresh and Connection check
    private void startConnectionCheck(){
        connectionCheckTimeline = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> checkConnection()));
        connectionCheckTimeline.setCycleCount(Animation.INDEFINITE);
        connectionCheckTimeline.play();
    }

    private void batteryViewer() {
        batteryTimeline = new Timeline(new KeyFrame(
                Duration.millis(5000),
                ae -> setbatteryView()));
        batteryTimeline.setCycleCount(Animation.INDEFINITE);
        batteryTimeline.play();
    }

    private void tempViewer(){
        tempTimeline = new Timeline(new KeyFrame(
                Duration.millis(5000),
                    ae -> settempView()));
        tempTimeline.setCycleCount(Animation.INDEFINITE);
        tempTimeline.play();
    }

    public void set_switchRest (MouseEvent mouseEvent) {

        try {
            nao1.toggleRest();
            if(nao1.isInRestMode()){
                ts_rest.setSelected(true);
            } else {
                ts_rest.setSelected(false);
            }
        } catch (ConnectionException e) {
            connectionLost();
        }
    }

    public void toggle_Camera (){
        nao1.toggleCamera();
        if(nao1.isCameraActivated()){
            imageView11.setVisible(true);
            ts_camera.setSelected(true);
        } else {
            imageView11.setVisible(false);
            ts_camera.setSelected(false);
        }

    }


    //#####################  ARM-CONTROL #########################
    //Buttons TFGH for arm-control
    public void arm_up(ActionEvent actionEvent) {

        try {
            if (armModeSide == 2) {
                nao1.moveArm(armControl1[0][armModeJoint], -1);
                nao1.moveArm(armControl1[1][armModeJoint], -1);
            } else nao1.moveArm(armControl1[armModeSide][armModeJoint], -1);
        } catch (ConnectionException e) {
            connectionLost();
        }

    }

    public void arm_down(ActionEvent actionEvent) {
        try {
            if (armModeSide == 2) {
                nao1.moveArm(armControl1[0][armModeJoint], 1);
                nao1.moveArm(armControl1[1][armModeJoint], 1);
            } else nao1.moveArm(armControl1[armModeSide][armModeJoint], 1);
        } catch (ConnectionException e) {
                connectionLost();
        }
    }

    public void arm_left(ActionEvent actionEvent) {
        try {
            if (armModeSide == 2) {
                nao1.moveArm(armControl2[0][armModeJoint], -1);
                nao1.moveArm(armControl2[1][armModeJoint], -1);
            } else nao1.moveArm(armControl2[armModeSide][armModeJoint], -1);
        } catch (ConnectionException e){
            connectionLost();
        }
    }

    public void arm_right(ActionEvent actionEvent) {
        try {
            if(armModeSide == 2){
                nao1.moveArm(armControl2[0][armModeJoint],1);
                nao1.moveArm(armControl2[1][armModeJoint],1);
            } else nao1.moveArm(armControl2[armModeSide][armModeJoint],1);
        } catch (ConnectionException e){
            connectionLost();
        }
    }

    public void handSelect(MouseEvent mouseEvent) {
        if (ts_hand.isSelected()) {
            ts_hand.setDisable(true);
            ts_shoulder.setDisable(false);
            ts_shoulder.setSelected(false);
            ts_elbow.setDisable(false);
            ts_elbow.setSelected(false);
            armModeJoint = 2;
        }
    }

    public void shoulderSelect(MouseEvent mouseEvent) {
        if (ts_shoulder.isSelected()) {
            ts_shoulder.setDisable(true);
            ts_hand.setSelected(false);
            ts_hand.setDisable(false);
            ts_elbow.setSelected(false);
            ts_elbow.setDisable(false);
            armModeJoint = 0;
        }
    }

    public void elbowSelect(MouseEvent mouseEvent) {
        if (ts_elbow.isSelected()) {
            ts_elbow.setDisable(true);
            ts_hand.setSelected(false);
            ts_hand.setDisable(false);
            ts_shoulder.setSelected(false);
            ts_shoulder.setDisable(false);
            armModeJoint = 1;
        }
    }

    public void armLeftChecked(MouseEvent mouseEvent) {
        armModeSide = 1;
        chb_left.setDisable(true);
        chb_right.setSelected(false);
        chb_right.setDisable(false);
        chb_mirror_arm.setSelected(false);
        chb_mirror_arm.setDisable(false);
    }

    public void armRightChecked(MouseEvent mouseEvent) {
        armModeSide = 0;
        chb_right.setDisable(true);
        chb_left.setSelected(false);
        chb_left.setDisable(false);
        chb_mirror_arm.setSelected(false);
        chb_mirror_arm.setDisable(false);
    }

    public void armMirrorChecked(MouseEvent mouseEvent) {
        armModeSide = 2;
        chb_mirror_arm.setDisable(true);
        chb_left.setSelected(false);
        chb_left.setDisable(false);
        chb_right.setSelected(false);
        chb_right.setDisable(false);
    }

    public void toggle_ArmMovmentDuringWalking (){
        try {
            nao1.toggleMoveArms();
            if(nao1.isMoveArmsEnabled()){
                ts_armMovement.setSelected(true);
            } else {
                ts_armMovement.setSelected(false);
            }
        } catch (ConnectionException e) {
            connectionLost();
        }
    }


    //When clicking on main-pane then deselect textfields etc...
    @FXML
    private void setFocus(MouseEvent mouseEvent) {
        pane_main.requestFocus();
    }
}