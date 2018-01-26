package GUI;

import NAO.NAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginController{

    public static NAO nao1;

    private String robotURL;

    @FXML
    public Button btn_connect;
    public Button btn_close;
    public Button btn_delete;
    public TextField txt_port;
    public TextField txt_ipaddress;
    public ListView<String> connection_list;

    //Constructor
    public LoginController(){

        //Führt Methode "saveConfig" bei Schließen des Programms (des Threads) aus
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                store();
            }
        }));

    }

    @FXML
    public void initialize(){
    Configurator.loader();
        List<String> connections1 = new ArrayList<String>(Arrays.asList(Configurator.props.get("urls").toString().split("\\,")));
    fillConnectionList(connections1);
    }

    private void fillConnectionList(List<String> inputList) {
        ObservableList<String> insert = FXCollections.observableArrayList(inputList);
        connection_list.setItems(insert);
    }

    public void connect(ActionEvent actionEvent) {
            createRobotUrl();
            nao1 = new NAO();
            nao1.establishConnection(robotURL);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("../GUI/main_scene.fxml"));
            Stage rootWindow = new Stage();
            rootWindow.setScene(new Scene(root));
            rootWindow.show();
            Stage thisWindow = (Stage) btn_connect.getScene().getWindow();
            thisWindow.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void store(){
        if(!txt_port.getText().isEmpty() && !txt_ipaddress.getText().isEmpty()){
            createRobotUrl();
            connection_list.getItems().add(robotURL);
        }
        String connectionListString = String.join(",",connection_list.getItems());
        Configurator.saver("urls",connectionListString);
    }

    public void fill_txt(MouseEvent mouseEvent) throws NullPointerException{
        String URL = connection_list.getSelectionModel().getSelectedItem();
        String[] parts = URL.split(":");
        String HOST = parts[1].replaceAll("//","");
        int PORT = Integer.parseInt(parts[2]);
        txt_ipaddress.setText(HOST);
        txt_port.setText(String.valueOf(PORT));
    }

    public void deleteEntry(ActionEvent actionEvent) {
        int selectedIdx = connection_list.getSelectionModel().getSelectedIndex();
        connection_list.getItems().remove(selectedIdx);
    }

    private void createRobotUrl() {
        //neue Instanz von InputParse
        InputParse parser = new InputParse();
        //Variable warning für Ausgabe der Fehlermeldung
        String warning = "";

        //Erzeugen der Warnmeldung, falls Eingaben nicht in RegEx passen
        if (!parser.validateIP(txt_ipaddress.getText()) || !parser.validatePort(txt_port.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong Input");
            alert.setHeaderText("Please check your Input");
            //Feld IP-Adresse leer:
            if (txt_ipaddress.getText().isEmpty()) {
                warning = "Please type in an IP address!";
            } //Feld IP-Adresse falsche Eingabe:
            else if (!parser.validateIP(txt_ipaddress.getText())) {
                warning = txt_ipaddress.getText() + " is not a valid IP address!";
            } //Feld Port leer:
            if (txt_port.getText().isEmpty()) {
                warning = warning + "\n" + "Please type in a port number!";
            } //Feld Port falsche Eingabe:
            else if (!parser.validatePort(txt_port.getText())) {
                warning = warning + "\n" + txt_port.getText() + " is not a valid port number!";
            } //Setzen der Warnmeldung und Anzeigen des Fehler-Dialogs
            alert.setContentText(warning);
            alert.showAndWait();
        } else { //Falls Eingaben korrekt, Connection öffnen:
            robotURL = "tcp://" + txt_ipaddress.getText() + ":" + txt_port.getText();
        }
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }
}
