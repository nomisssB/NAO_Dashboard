package gui;
/*
* FILE: ConfigHelper.java
* USAGE: Helper methods for saving/loading configs from/to a .dashboard-file in the root-folder
*/
import java.io.*;
import java.util.Properties;

class ConfigHelper {

    // Variables declaration
    static Properties props = new Properties();                             // Object to store "key-value" configs
    private static File configFile = new File("config.dashboard");  // Config-file

    static void saver(String key, String value){ // saves (one) key-value pair to .dashboard file
        try {
            props.setProperty(key, value);  // stores key-value to Properties-object
            FileOutputStream fileOut = new FileOutputStream(configFile);
            props.store(fileOut, "Configuration for NAO Dashboard by MKSM"); // writes key-values from Properties-Object to .dashboard-file
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void loader(){ // loads all key-values to Properties-object
        try {
            FileInputStream fileInput = new FileInputStream(configFile);
            props.load(fileInput);
            fileInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
