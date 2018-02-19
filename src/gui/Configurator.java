package gui;
/*
FILE: Configurator.java
USAGE: Controls saving/loading configs from/to a .dashboard-file in the root-folder
 */
import java.io.*;
import java.util.Properties;

class Configurator {

    //Variables declaration
    static Properties props = new Properties();                             //object to store "key-value" configs
    private static File configFile = new File("config.dashboard");  //config-file

    static void saver(String key, String value){ //saves (one) key-value pair to .dashboard file
        try {
            props.setProperty(key, value);  //stores key-value to Properties-object
            FileOutputStream fileOut = new FileOutputStream(configFile);
            props.store(fileOut, "Configuration for NAO Dashboard by MKSM"); //writes key-values from Properties-Object to .dashboard-file
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void loader(){
        try {
            FileInputStream fileInput = new FileInputStream(configFile);
            props.load(fileInput); //loads all key-values to Properties-object
            fileInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
