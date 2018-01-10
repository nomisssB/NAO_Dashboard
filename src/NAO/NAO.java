package NAO;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import javafx.event.ActionEvent;

import java.util.EmptyStackException;
import java.util.List;


public class NAO {
    //Variabeln Deklarationen
    private static Application app; // = new Application(new String[] {});
    private static ALMotion motion;
    private static ALTextToSpeech tts;



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
            System.out.println("App has been started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void closeConnection(){
        app.session().close();
    }*/

    public void sayText(String text) throws ConnectionException {
        checkConnection();
        try {
            tts = new ALTextToSpeech(app.session());
            tts.say(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveHead(float left, float right, float down, float up) throws ConnectionException{ // unfertig #testing
        checkConnection();
        try {
            motion = new ALMotion(app.session());
            motion.stiffnessInterpolation("HeadYaw", 1.0f, 0.1f);
            motion.angleInterpolation("HeadYaw", left-right, 0.1f, false);
            motion.angleInterpolation("HeadPitch", up-down, 0.1f, false);
            motion.stiffnessInterpolation("HeadYaw", 0.0f, 0.1f);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void checkConnection() throws ConnectionException{
        if (app.session() == null) {
            throw new ConnectionException();
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
