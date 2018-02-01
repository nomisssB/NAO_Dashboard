package GUI;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class Configurator {

    static Properties props = new Properties();
    private static File configFile = new File("config.dashboard");

    static void saver(String key, String value){
        try {
        props.setProperty(key, value);
        FileOutputStream fileOut = new FileOutputStream(configFile);
        props.store(fileOut, "Configuration for NAO Dashboard");
        fileOut.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    static void loader(){
        try {
            FileInputStream fileInput = new FileInputStream(configFile);
            props.load(fileInput);
            fileInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
