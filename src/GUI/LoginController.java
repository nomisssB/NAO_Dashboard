package GUI;

import NAO.NAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController{

    public Stage rootWindow;
    public static NAO nao1;
    private Stage thisWindow;
    private String robotURL;

    @FXML
    public Button btn_connect;
    public TextField txt_port;
    public TextField txt_ipaddress;
    public ListView connection_list;


    public void connect(ActionEvent actionEvent) {
        //neue Instanz von InputParse
        InputParse parser = new InputParse();
        //Variable warning für Ausgabe der Fehlermeldung
        String warning="";

        //Erzeugen der Warnmeldung, falls Eingaben nicht in RegEx passen
        if(!parser.validateIP(txt_ipaddress.getText()) || !parser.validatePort(txt_port.getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong Input");
            alert.setHeaderText("Please check your Input");
            //Feld IP-Adresse leer:
            if(txt_ipaddress.getText().isEmpty()){
                warning = "Please type in an IP address!";
            } //Feld IP-Adresse falsche Eingabe:
            else if (!parser.validateIP(txt_ipaddress.getText())) {
                warning = txt_ipaddress.getText() + " is not a valid IP address!";
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
            robotURL = "tcp://" + txt_ipaddress.getText() + ":" + txt_port.getText();
            nao1 = new NAO();
            nao1.establishConnection(robotURL);
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("../GUI/main_scene.fxml"));
            rootWindow = new Stage();
            rootWindow.setScene(new Scene(root));
            rootWindow.show();
            thisWindow = (Stage) btn_connect.getScene().getWindow();
            thisWindow.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
