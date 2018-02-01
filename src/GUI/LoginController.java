package GUI;

/*
FILE: LoginController.java
USAGE: Controls "Login-Window" (Connection establishment)
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
    Configurator.loader();// loads settings from config-file
        if(!Configurator.props.get("urls").toString().equals("")){
            List<String> connections1 = new ArrayList<String>                    //temporary list with last connections
                    (Arrays.asList(Configurator.props.get("urls").toString().split("\\,")));
            //all URLs of the last connections are stored as one comma seperated string in config-file
            //first they had to be split in single items
            fillConnectionList(connections1);//fills ListView with items
        }
    }

    private void fillConnectionList(List<String> inputList) {
        ObservableList<String> insert = FXCollections.observableArrayList(inputList); //Observable list is needed for javafx ListView
        connection_list.setItems(insert); //fills ListView with items
    }

    public void connect(ActionEvent actionEvent) {
        if(!createRobotUrl()) return; // parses correct IP and Port
        if(!checkDuplicate(robotURL,connection_list.getItems())) {
            store(); //Check for an already existing entry in connection list
        }
            nao1 = new NAO(); // new Instance of NAO-class
            if(!nao1.establishConnection(robotURL)) {
                Alert alert = new Alert(Alert.AlertType.WARNING); //When the connection couldn't be established
                alert.setTitle("Connection failed!");
                alert.setHeaderText("Something went wrong...");
                alert.setContentText("Connection is not established. \nThis could be caused due to different reasons..." +
                        "\ne.g. wrong WiFi-Network or ip-address/port");
                alert.showAndWait();
                }else { //when connection was successful
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("../GUI/main_scene.fxml"));
                    rootWindow = new Stage(); //create Main-Stage
                    rootWindow.setScene(new Scene(root));
                    rootWindow.show();
                    loginWindow.hide(); //hide login-Window
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }

    private void store(){ //store connection list to .dashboard - file
        connection_list.getItems().add(robotURL);

        if(!connection_list.getItems().isEmpty()){ //store only when list isn't empty
            String connectionListString = String.join(",",connection_list.getItems());
            Configurator.saver("urls",connectionListString);
        }

    }

    public void fill_txt(MouseEvent mouseEvent) throws NullPointerException{
        if(!connection_list.getItems().isEmpty()) { //only when ListView isn't empty
            String URL = connection_list.getSelectionModel().getSelectedItem();
            String[] parts = URL.split(":"); //split String to its parts
            String HOST = parts[1].replaceAll("//", ""); //kill slashes
            int PORT = Integer.parseInt(parts[2]); //store second split-part to PORT as int
            txt_ipaddress.setText(HOST);
            txt_port.setText(String.valueOf(PORT));
        }
    }

    public void deleteEntry(ActionEvent actionEvent) { //remove selected ListView - item
        int selectedIdx = connection_list.getSelectionModel().getSelectedIndex();
        connection_list.getItems().remove(selectedIdx);
    }

    private boolean createRobotUrl() {
        InputParse parser = new InputParse();       //new instance of InputParser
        String warning = ""; //Variable warning for alert-message

        //Create alert-message, if user-inputs aren't valid ip-address/port
        if (!parser.validateIP(txt_ipaddress.getText()) || !parser.validatePort(txt_port.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong Input");
            alert.setHeaderText("Please check your Input");
            //textfield ip-address empty:
            if (txt_ipaddress.getText().isEmpty()) {
                warning = "Please type in an IP address!";
            } //textfield ip-address wrong input:
            else if (!parser.validateIP(txt_ipaddress.getText())) {
                warning = txt_ipaddress.getText() + " is not a valid IP address!";
            } //textfield port empty:
            if (txt_port.getText().isEmpty()) {
                warning = warning + "\n" + "Please type in a port number!";
            } //textfield port wrong input:
            else if (!parser.validatePort(txt_port.getText())) {
                warning = warning + "\n" + txt_port.getText() + " is not a valid port number!";
            } //bind alert-message to alert-dialogue and show it
            alert.setContentText(warning);
            alert.showAndWait();
            return false;
        } else { //if all inputs are correct, create URL for connection:
            robotURL = "tcp://" + txt_ipaddress.getText() + ":" + txt_port.getText();
            return true;
        }
    }

    public void close(ActionEvent actionEvent) { //shut the program
        System.exit(0);
    }

    private boolean checkDuplicate(String value, List<String> list) { //Searches for a String in a List<String>
        for (String string : list) { //every item in the list...
            if (string.matches(value)) { //compare
                return true;
            }
        }
        return false;
    }

}
