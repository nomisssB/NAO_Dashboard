package naoDash_main;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


public class Controller {
    public double motionspeed = 0;

    @FXML
    public VBox vbox_main;
    public Slider sldr_speed;
    public Button btn_s;
    public Button btn_a;
    public Button btn_d;
    public Button btn_w;
    public Button btn_connect;
    public Label lbl_mid;


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
            lbl_mid.setText("value: " + newValue.intValue());
            motionspeed = newValue.intValue();

        });



        //Abfangen von KeyEvents
        vbox_main.setOnKeyPressed(e ->{
            switch (e.getCode()){
                case W:  logger.info("Button W");btn_w.fire(); break;
                case A: logger.info("Button A");btn_a.fire(); break;
                case S: logger.info("Button S");btn_s.fire(); break;
                case D: logger.info("Button D");btn_d.fire(); break;
            }
        });

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
}


