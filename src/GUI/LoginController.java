package GUI;

/*
FILE: LoginController.java
USAGE: Controls "Login-Window" (Connection establishment)

TODO may be "robot-names"
 */

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
import static naoDash_main.Main.loginWindow;

public class LoginController{

    public static NAO nao1;  //Instance of NAO-class, which will be created due to the connection establishment
    private String robotURL; //String for calling "establishConnection" method
    public static Stage rootWindow; //Static string for the main dashboard

    @FXML   // FXML-Annotations
    public Button btn_connect;
    public Button btn_close;
    public Button btn_delete;
    public Button btn_test;
    public TextField txt_port;
    public TextField txt_ipaddress;
    public ListView<String> connection_list;


    public LoginController(){      //Constructor
//  NOT IN USE:  Runtime.getRuntime().addShutdownHook(new Thread(this::store));
// stores current settings to config-file when closing the program
    }

    @FXML
    public void initialize(){     // is called after Construktor and FXML annotations
    Configurator.loader();                                                  // loads settings from config-file
        List<String> connections1 = new ArrayList<String>                    //temporary list with last connections
                (Arrays.asList(Configurator.props.get("urls").toString().split("\\,")));
                //all URLs of the last connections are stored as one comma seperated string in config-file
                //first they had to be split in single items
    fillConnectionList(connections1);                                       //fills ListView with items
    }

    private void fillConnectionList(List<String> inputList) {
        ObservableList<String> insert = FXCollections.observableArrayList(inputList); //Observable list is needed for javafx ListView
        connection_list.setItems(insert); //fills ListView with items
    }

    public void connect(ActionEvent actionEvent) {
        if(!createRobotUrl()) return;
        if(!checkDuplicate(robotURL,connection_list.getItems())) {
            store();
        }
            nao1 = new NAO();
            if(!nao1.establishConnection(robotURL)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Connection failed!");
                alert.setHeaderText("Something went wrong...");
                alert.setContentText("Connection is not established. \nThis could be caused due to different reasons..." +
                        "\ne.g. wrong WiFi-Network or ip-address/port");
                alert.showAndWait();
                return;
            }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("../GUI/main_scene.fxml"));
            rootWindow = new Stage();
            rootWindow.setScene(new Scene(root));
            rootWindow.show();
            loginWindow.hide();
            store();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void store(){
        connection_list.getItems().add(robotURL);
        if(!connection_list.getItems().isEmpty()){
            String connectionListString = String.join(",",connection_list.getItems());
            Configurator.saver("urls",connectionListString);
        }

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

    private boolean createRobotUrl() {
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
            return false;
        } else { //Falls Eingaben korrekt, Connection öffnen:
            robotURL = "tcp://" + txt_ipaddress.getText() + ":" + txt_port.getText();
            return true;
        }
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    private boolean checkDuplicate(String value, List<String> list) { //Searches for a String in a List<String>
        for (String string : list) {
            if (string.matches(value)) {
                return true;
            }
        }
        return false;
    }

}
