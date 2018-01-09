package NAO;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import javafx.event.ActionEvent;


public class NAO {
    //Variabeln Deklarationen
    private Application app;


    public boolean establishConnection(String url){
        if (app != null) { // Check if there is already a Connection
            closeConnection(); // and close it, if so.
            System.out.println("Connection closed");
        }
        app = new Application (new String[] {}, url);
        try {
            app.start();
        } catch(Exception e){
            System.out.println("failed");
            return false;
        }
        System.out.println("success");
        return true;
    }

    public void closeConnection(){
        app.stop();
    }





}
