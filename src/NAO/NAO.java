package NAO;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import javafx.event.ActionEvent;

import java.util.EmptyStackException;
import java.util.List;


public class NAO {
    //Variabeln Deklarationen
    private static Application app; // = new Application(new String[] {});



    public void establishConnection(String url) {

        // Versuch die Verbindung mehrmals aufzubauen / wiederaufzubauen, funktioniert aktuell nicht.
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

    public void sayText(String text) throws ConnectionException {
        if (app.session() == null) {
            throw new ConnectionException();
        }
        try {
            ALTextToSpeech tts = new ALTextToSpeech(app.session());
            tts.say(text);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getPostures() throws Exception {
        ALRobotPosture moves = new ALRobotPosture();
        List<String> postures = moves.getPostureList();

        for(int i = 0;i<postures.size();i++){
        System.out.println(postures.get(i).toString());
        }
    }





}
