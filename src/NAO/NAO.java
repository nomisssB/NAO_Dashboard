package NAO;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.proxies.*;
import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import javafx.scene.paint.Color;

public class NAO {
    //Variabeln Deklarationen
    private static Application app;
    private static ALMotion motion;
    private static ALTextToSpeech tts;
    private static ALRobotPosture pose;
    private static ALLeds led ;
    private static ALBattery bat;
    private static float moveX; // movement forwards / backwards
    private static float moveY; // movement sideways
    private static float moveT; // movement spinning
    private static float moveV; // velocity of movement

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
                // Initialisation of the Connection to the NAO
                app = new Application(new String[]{}, url);
                app.start();
                // Initialisation of the NAO Control Objekts.
                motion = new ALMotion(app.session());
                tts = new ALTextToSpeech(app.session());
                pose = new ALRobotPosture(app.session());
                led = new ALLeds(app.session());
                bat = new ALBattery(app.session());
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


    public void moveHead(float left, float right, float down, float up) throws ConnectionException{ // unfertig #testing
        checkConnection();
        try {
            motion.angleInterpolation("HeadYaw", left-right, 0.1f, false); // move Head left or right
            motion.angleInterpolation("HeadPitch", down-up, 0.1f, false); // move Head up or down
        } catch(Exception e) {
            e.printStackTrace();
        }


    }

    public void moveHead(String direction) throws ConnectionException{
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
            motion.angleInterpolation(joint, move, 0.01f, false); // move the head with the values from above
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sayText(String text) throws ConnectionException {
        checkConnection();
        try {
            tts.say(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sayText(String text, String voice, float volume, float pitch) throws ConnectionException {
        checkConnection();
        try {
            tts.setVoice(voice);
            tts.setVolume(volume);
            tts.setParameter("pitchShift", pitch);
            tts.say(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getLanguages() throws ConnectionException { // Returns all installed languages
        checkConnection();
        try {
            return tts.getAvailableLanguages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getVoices() throws ConnectionException { // Returns all installed voices
        checkConnection();
        try {
            return tts.getAvailableVoices();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void execPosture(String posture) throws ConnectionException { // Goto a given Posture
        checkConnection();
        try {
            pose.goToPosture(posture, 1.0f);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getPostures() throws ConnectionException { // Returns all possible postures
        checkConnection();
        try {
            return pose.getPostureList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void moveToward( float x, float y, float thata, float v) throws ConnectionException {
        checkConnection();

        x = x*v;
        y = y*v;
        thata = thata*v;

        try {
            motion.moveToward(x, y, thata);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMoveX (float x) throws ConnectionException { // set the x direction of motion and
        checkConnection();                        //  recall the moveToward method to let the NAO move

        if (moveY == 0) { // Nao can only move in x or y. If both are set to move at once, nothing happens
            moveX = x;
        }
        try {
            // set the new movement directions combined with the speed.
            motion.moveToward(moveX * moveV, moveY * moveV, moveT * moveV);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMoveY (float y) throws ConnectionException {// set the y direction of motion and
        checkConnection();                      //  recall the moveToward method to let the NAO move

        if (moveX == 0) { // Nao can only move in x or y. If both are set to move at once, nothing happens
            moveY = y;
        }
        try {
            // set the new movement directions combined with the speed.
            motion.moveToward(moveX * moveV, moveY * moveV, moveT * moveV);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMoveT (float t) throws ConnectionException {
        checkConnection();

        moveT = t; // NAO can spin and walk in one direction at once, so just let him spin.

        try {
            // set the new movement directions combined with the speed.
            motion.moveToward(moveX * moveV, moveY * moveV, moveT * moveV);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMoveV (float v) throws ConnectionException { // sets the speed of the movement
        checkConnection();
        moveV = v;

        try {
            // set the new speed combined with the movement directions
            motion.moveToward(moveX * moveV, moveY * moveV, moveT * moveV);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void moveArm( String joint, float direction) throws ConnectionException{
        checkConnection();
        try {
            motion.angleInterpolation(joint, 0.1f*direction, 0.1f, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void changeEyeColor(String eye, float red, float green, float blue) throws ConnectionException{
        checkConnection();

        try {
            if(eye == "Right" ) {
                led.fadeRGB("RightFaceLeds", red, green, blue, 0f);
            }else if(eye == "Left") {
                led.fadeRGB("LeftFaceLeds", red, green, blue, 0f);
            }else if ( eye == "Both") {
                led.fadeRGB("RightFaceLeds", red, green, blue, 0f);
                led.fadeRGB("LeftFaceLeds", red, green, blue, 0f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeEyeColor(String eye, Color color) throws ConnectionException{
        checkConnection();
        float red = (float) color.getRed();
        float green = (float) color.getGreen();
        float blue = (float) color.getBlue();

        try {
            if(eye == "Right" ) {
                led.fadeRGB("RightFaceLeds", red, green, blue, 0f);
            }else if(eye == "Left") {
                led.fadeRGB("LeftFaceLeds", red, green, blue, 0f);
            }else if ( eye == "Both") {
                led.fadeRGB("RightFaceLeds", red, green, blue, 0f);
                led.fadeRGB("LeftFaceLeds", red, green, blue, 0f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double batteryPercent() throws InterruptedException{       //Get the battery charge in percents

        try {
            bat = new ALBattery(app.session());
            double charge = (double) bat.getBatteryCharge() / 100;
            return charge;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}

