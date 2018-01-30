package GUI;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Configurator {

    public static Properties props = new Properties();
    public static File configFile = new File("config.dashboard");

    public static void saver (String key, String value){
        try {
        props.setProperty(key, value);
        FileOutputStream fileOut = new FileOutputStream(configFile);
        props.store(fileOut, "Configuration for NAO Dashboard");
        fileOut.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public static void loader (){
        try {
            FileInputStream fileInput = new FileInputStream(configFile);
            props.load(fileInput);
            fileInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
