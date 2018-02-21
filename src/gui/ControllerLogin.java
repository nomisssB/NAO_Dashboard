package gui;
/*
* FILE: ControllerLogin.java
* USAGE: Controls "Login-Window" (Connection establishment)
*/
import nao.NAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static main.Main.loginWindow;

public class ControllerLogin {

    // Variables declaration
    static NAO nao1;  // Var of NAO, instance will be created due to the connection establishment
    private String robotURL; // String for calling "establishConnection" method
    static Stage rootWindow; // Static Stage for main window

    // FXML-Annotations
    @FXML
    private TextField txt_port;
    @FXML
    private TextField txt_ipaddress;
    @FXML
    private ListView<String> connection_list;

    @FXML
    public void initialize(){     // is called after constructor and FXML annotations
    ConfigHelper.loader();// loads settings from config-file in Properties-object
        if(!ConfigHelper.props.get("urls").toString().equals("")){  //only when "urls"-value is not empty...
            List<String> connections1 = new ArrayList<>            // temporary list with last connections
                    (Arrays.asList(ConfigHelper.props.get("urls").toString().split("\\,")));
            /* all last connections are stored as one comma seperated string in dashboard-file
            * first: split in single items and save in ArrayList*/
            fillConnectionList(connections1);// fills ListView with items out of the ArrayList
        }
    }

    // ######################### Establish Connection #####################
    public void connect(ActionEvent actionEvent) {
        if(!createRobotUrl()) return; // parses correct IP and Port
        nao1 = new NAO(); // new Instance of NAO-class, will be forwarded to Main-Controller
        if(!nao1.establishConnection(robotURL)) {
            Alert alert = new Alert(Alert.AlertType.WARNING); // when connection couldn't be established
            alert.setTitle("Connection failed!");
            alert.setHeaderText("Something went wrong...");
            alert.setContentText("Connection is not established. \nThis could be caused due to different reasons..." +
                    "\ne.g. wrong WiFi-Network or ip-address/port");
            alert.showAndWait();
        } else { // when connection was successfully established
            if(!checkDuplicate(robotURL,connection_list.getItems())) { // Check for an already existing entry in connection list
                connection_list.getItems().add(robotURL);
                store();
            }
            try {
                Parent root = FXMLLoader.load(getClass().getResource("scene_main.fxml"));
                rootWindow = new Stage(); // create stage for main-window
                rootWindow.setScene(new Scene(root));
                rootWindow.setResizable(false); // static window size
                rootWindow.show(); // shows main-window
                loginWindow.hide(); // hides current-window (login-window)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ######################### filler ###################################
    private void fillConnectionList(List<String> inputList) {
        ObservableList<String> insert = FXCollections.observableArrayList(inputList); // Observable list needed for javafx ListView
        connection_list.setItems(insert); // fills ListView with items
    }

    public void fillTxt(MouseEvent mouseEvent) throws NullPointerException{
        if(!connection_list.getItems().isEmpty()) { // only when ListView isn't empty
            String URL = connection_list.getSelectionModel().getSelectedItem();
            String[] parts = URL.split(":"); // split String to its parts
            String HOST = parts[1].replaceAll("//", ""); // kill slashes
            int PORT = Integer.parseInt(parts[2]); // store second split-part to PORT as int
            txt_ipaddress.setText(HOST);
            txt_port.setText(String.valueOf(PORT));
        }
    }


    // ######################### List control #############################
    public void deleteEntry(ActionEvent actionEvent) { // remove selected ListView - item
        int selectedIdx = connection_list.getSelectionModel().getSelectedIndex();
        connection_list.getItems().remove(selectedIdx);
    }


    // ######################### Helpers ##################################
    private boolean createRobotUrl() {
        InputHelper parser = new InputHelper();     // new instance of InputParser
        String warning = "";                        // Variable warning for alert-message

        // Create alert-message, if user-inputs aren't valid ip-address/port
        if (!parser.validateIP(txt_ipaddress.getText()) || !parser.validatePort(txt_port.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong Input");
            alert.setHeaderText("Please check your Input");

            // textfield ip-address empty:
            if (txt_ipaddress.getText().isEmpty()) {
                warning = "Please type in an IP address!";
            }
            // textfield ip-address wrong input:
            else if (!parser.validateIP(txt_ipaddress.getText())) {
                warning = txt_ipaddress.getText() + " is not a valid IP address!";
            }
            // textfield port empty:
            if (txt_port.getText().isEmpty()) {
                warning = warning + "\n" + "Please type in a port number!";
            }
            // textfield port wrong input:
            else if (!parser.validatePort(txt_port.getText())) {
                warning = warning + "\n" + txt_port.getText() + " is not a valid port number!";
            }

            // bind alert-message to alert-dialogue and show it
            alert.setContentText(warning);
            alert.showAndWait();
            return false;
        } else { // if all inputs are correct, create URL for connection:
            robotURL = "tcp://" + txt_ipaddress.getText() + ":" + txt_port.getText();
            return true;
        }
    }

    private boolean checkDuplicate(String value, List<String> list) { // searches for a String in a List<String>
        for (String string : list) { // every String-item in the list...
            if (string.matches(value)) { // compare
                return true;
            }
        }
        return false;
    }

    private void store(){ // stores connection list to .dashboard - file

        if(!connection_list.getItems().isEmpty()){ // stores the list only when it is not empty
            String connectionListString = String.join(",",connection_list.getItems()); // whole list in one String
            ConfigHelper.saver("urls",connectionListString);
        }

    }

    // ######################### Misc #####################################
    public void close(ActionEvent actionEvent) { // close program
        store();
        System.exit(0);
    }
}
