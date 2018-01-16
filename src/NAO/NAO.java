package NAO;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALLeds;
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
    private static ALRobotPosture pose;



    public void establishConnection(String url) {
        if ( app != null){ // falls Verbindung schon besteht, soll sie neu aufgebaut werden
            closeConnection(); // funktioniert allerdings noch nicht.
            try {
                app.session().connect(url);
                app.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { // Establish a new connection, if there wasn't one yet.
            try {
                app = new Application(new String[]{}, url);
                app.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void closeConnection(){
        if ( app != null) {
            app.session().close();
            app.stop();
        }
    }

    public void checkConnection() throws ConnectionException{
        if (app.session() == null) {
            throw new ConnectionException();
        }
    }

    public void rasta() throws ConnectionException{ // first LED test, but not working with Choregraphe
        checkConnection();
        try {
            ALLeds led = new ALLeds(app.session());
            led.rasta(3.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void moveHead(float left, float right, float down, float up) throws ConnectionException{ // unfertig #testing
        // not save, if motion.stiffnessInterpolation is needed. Should be tested with real NAO. (TODO)
        checkConnection();
        try {
            motion = new ALMotion(app.session());
            // motion.stiffnessInterpolation("HeadYaw", 1.0f, 0.1f); // Apparently not needed, but kept here, if needed later
            motion.angleInterpolation("HeadYaw", left-right, 0.1f, false); // move Head left right
            motion.angleInterpolation("HeadPitch", down-up, 0.1f, false); // move Head up down
            //motion.stiffnessInterpolation("HeadYaw", 0.0f, 0.1f); // Apparently not needed, but kept here, if needed later
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void moveHead(String direction) throws ConnectionException{
        // not save, if motion.stiffnessInterpolation is needed. Should be tested with real NAO. (TODO)
        checkConnection();
        float strongness = 0.05f; // how much does the head move per call
        String joint; // Moving horizontal ("HeadPitch") or vertical ("HeadYaw")
        float move; // moving up/right (negative value) or down/left (positive value)
        switch (direction){ // set variables for the correct movement
            case "up":
                move = -1*strongness;
                joint = "HeadPitch";
                break;
            case "down":
                move = strongness;
                joint = "HeadPitch";
                break;
            case "right":
                move = -1*strongness;
                joint = "HeadYaw";
                break;
            case "left":
                move = strongness;
                joint = "HeadYaw";
                break;
            default:
                move = 99f;
                joint = "HeadPitch";
                break;
        }
        try {
            motion = new ALMotion(app.session());
            motion.angleInterpolation(joint, move, 0.01f, false);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sayText(String text) throws ConnectionException {
        checkConnection();
        try {
            tts = new ALTextToSpeech(app.session());
            tts.say(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getLanguages() throws ConnectionException { // Returns all installed languages
        checkConnection();
        try {
            tts = new ALTextToSpeech(app.session());
            return tts.getAvailableLanguages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getVoices() throws ConnectionException { // Returns all installed voices
        checkConnection();
        try {
            tts = new ALTextToSpeech(app.session());
            return tts.getAvailableVoices();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void execPosture(String posture) throws ConnectionException {
        checkConnection();
        try {
            pose = new ALRobotPosture(app.session());
            pose.goToPosture(posture, 1.0f);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getPostures() throws ConnectionException { // Returns all possible postures
        checkConnection();
        ALRobotPosture moves = null;
        try {
            moves = new ALRobotPosture(app.session());
            return moves.getPostureList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
