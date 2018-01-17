package NAO;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.proxies.*;
import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class NAO {
    //Variabeln Deklarationen
    private static Application app; // = new Application(new String[] {});
    private static ALMotion motion;
    private static ALTextToSpeech tts;
    private static ALRobotPosture pose;
    private static ALLeds led ;
    private static ALBattery bat;

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
        // not save, if motion.stiffnessInterpolation is needed. Should be tested with real NAO. (TODO)
        checkConnection();
        try {
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
            motion.angleInterpolation(joint, move, 0.01f, false);
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

    public void execPosture(String posture) throws ConnectionException {
        checkConnection();
        try {
            pose.goToPosture(posture, 1.0f);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getPostures() throws ConnectionException { // Returns all possible postures
        checkConnection();
        ALRobotPosture moves = null;
        try {
            return moves.getPostureList();
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
            ArrayList<String> temp = new ArrayList<String>();
            temp.add("RightFaceLed1");
            temp.add("RightFaceLed2");
            temp.add("RightFaceLed3");
            temp.add("RightFaceLed4");
            temp.add("RightFaceLed5");
            temp.add("RightFaceLed6");
            temp.add("RightFaceLed7");
            led.createGroup( "RightEye" , temp );

            ArrayList<String> temp1 = new ArrayList<String>();
            temp1.add("LeftFaceLed1");
            temp1.add("LeftFaceLed2");
            temp1.add("LeftFaceLed3");
            temp1.add("LeftFaceLed4");
            temp1.add("LeftFaceLed5");
            temp1.add("LeftFaceLed6");
            temp1.add("LeftFaceLed7");
            led.createGroup("LeftEye" , temp1);

            if(eye == "Right" ) {
                led.fadeRGB("RightEye", red, green, blue, 0f);
            }else if(eye == "Left") {
                led.fadeRGB("LeftEye", red, green, blue, 0f);
            }else if ( eye == "Both") {
                led.fadeRGB("RightEye", red, green, blue, 0f);
                led.fadeRGB("LeftEye", red, green, blue, 0f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int batteryPercent() throws InterruptedException{       //Get the battery charge in percents

        try {
            bat = new ALBattery(app.session());
            return bat.getBatteryCharge();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


}

