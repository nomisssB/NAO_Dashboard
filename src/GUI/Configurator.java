package GUI;

import java.io.*;
import java.util.Properties;

public class Configurator {

    public static Properties props = new Properties();
    public static String configFile = "config.xml";


    public static void saver (String filename, String key, String value){
        try {
        props.setProperty(key, value);
        File file = new File(filename);
        FileOutputStream fileOut = new FileOutputStream(file);
        props.storeToXML(fileOut, "Configuration for NAO Dashboard");
        fileOut.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public static void loader (String filename){
        try {
            File file = new File(filename);
            FileInputStream fileInput = new FileInputStream(file);
            props.loadFromXML(fileInput);
            fileInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
