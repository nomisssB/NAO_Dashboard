package naoDash_main;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;


public class Controller {
    public double motionspeed = 0;
    @FXML
    public Slider sldr_speed;
    @FXML
    public Button btn_s;
    @FXML
    private Button btn_w;
    @FXML
    private Label lbl_mid;


    public Controller(){


    }

    @FXML
    public void initialize(){
        sldr_speed.valueProperty().addListener((observable, oldValue, newValue) -> {
            lbl_mid.setText("value: " + newValue.intValue());
        });
    }



    public void forward(ActionEvent actionEvent){
        lbl_mid.setText("forward");
    }

    public void left(ActionEvent actionEvent) {
        lbl_mid.setText("left");

    }

    public void setSpeed(ActionEvent actionEvent) {




    }
}


