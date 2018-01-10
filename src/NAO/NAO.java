package NAO;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import javafx.event.ActionEvent;

import java.util.EmptyStackException;


public class NAO {
    //Variabeln Deklarationen
    public static Application app; // = new Application(new String[] {});



    public static void establishConnection(String url) {

        // Versuch die Verbindung auch mehrmals aufzubauen, funktioniert aktuell nicht.
        /*if (app.session().isConnected()) { // Check if there is already a Connection
            closeConnection(); // and close it, if so.
            System.out.println("Connection closed");
        }
        try {
            app.session().connect(url); // Try Connection
            System.out.println("connection_success");
        } catch(Exception e){
            System.out.println("connection_failed");
            return false;               // Connection failed
        }
        return true;                    // Connection established
    }*/
        try {
            app = new Application(new String[]{}, url);
            app.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void closeConnection(){
        app.session().close();
    }*/

    public void sayText(String text) throws Exception {
        try {
            ALTextToSpeech tts = new ALTextToSpeech(app.session());
            tts.say(text);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





}
