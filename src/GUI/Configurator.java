package GUI;

import java.io.*;
import java.util.Properties;

public class Configurator {

    public static Properties props = new Properties();


    public void saver (String filename, String key, String value){
        try {
        props.setProperty(key, value);

        File file = new File(filename);
        FileOutputStream fileOut = new FileOutputStream(file);
        props.storeToXML(fileOut, "Configuration for NAO Dashboard");
        fileOut.close();

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public void loader (String filename){
        try {
            File file = new File(filename);
            FileInputStream fileInput = new FileInputStream(file);
            props.loadFromXML(fileInput);
            fileInput.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
