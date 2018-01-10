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
            // motion.stiffnessInterpolation("HeadYaw", 1.0f, 0.1f); // Apparently not needed, but kept here, if needed later
            motion.angleInterpolation("HeadYaw", left-right, 0.1f, false); // move Head left right
            motion.angleInterpolation("HeadPitch", down-up, 0.1f, false); // move Head up down
            //motion.stiffnessInterpolation("HeadYaw", 0.0f, 0.1f); // Apparently not needed, but kept here, if needed later
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void moveHead(String direction) throws ConnectionException{
        checkConnection();
        String joint; // Moving horizontal ("HeadPitch") or vertical ("HeadYaw")
        float move; // moving up/right (negative value) or down/left (positive value)
        switch (direction){ // set variables for the correct movement
            case "up":
                move = -0.1f;
                joint = "HeadPitch";
                break;
            case "down":
                move = 0.1f;
                joint = "HeadPitch";
                break;
            case "right":
                move = -0.1f;
                joint = "HeadYaw";
                break;
            case "left":
                move = 0.1f;
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

    public void checkConnection() throws ConnectionException{
        if (app.session() == null) {
            throw new ConnectionException();
        }
    }


    public void getPostures() throws ConnectionException {
        checkConnection();
        ALRobotPosture moves = null;
        try {
            moves = new ALRobotPosture(app.session());
            List<String> postures = moves.getPostureList();
            for(int i = 0;i<postures.size();i++){
                System.out.println(postures.get(i).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
