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
    @FXML
    public Slider sldr_speed;
    @FXML
    public Button btn_s;
    @FXML
    public Button btn_w;
    @FXML
    public Label lbl_mid;


    public Controller(){


    }

    @FXML
    public void initialize() throws IOException {
        //Logger um Events und Fehler zu dokumentieren. Log Datei unter root\main_log.log (XML)
        FileHandler handler = new FileHandler("main_log.log", true);
        Logger logger = Logger.getLogger("NAODash Logger");
        logger.addHandler(handler);

        sldr_speed.valueProperty().addListener((observable, oldValue, newValue) -> {
            lbl_mid.setText("value: " + newValue.intValue());

        });



        //Abfangen von KeyEvents
        vbox_main.setOnKeyPressed(e ->{
            if(e.getCode() == KeyCode.W) {
                logger.info("Button W pressed and fired");
                btn_w.fire();
            }
        });

    }



    public void forward(ActionEvent actionEvent){
        lbl_mid.setText("forward");

    }

    public void left(ActionEvent actionEvent) {
        lbl_mid.setText("left");

    }
    
}


