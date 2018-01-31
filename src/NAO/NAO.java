package NAO;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import javafx.scene.paint.Color;

public class NAO {
    //Variabeln Deklarationen
    private static Session session;
    private static ALMotion motion;
    private static ALTextToSpeech tts;
    private static ALRobotPosture pose;
    private static ALLeds led;
    private static ALBattery bat;
    private static ALMemory memory;
    private static ALBodyTemperature temp;
    private static ALAudioPlayer play;
    private static ALAudioDevice audioDevice;
    private static float moveX; // movement forwards / backwards
    private static float moveY; // movement sideways
    private static float moveT; // movement spinning (T = Theta)
    private static float moveV; // velocity of movement
    public static String url;
    private String tactileHeadTextFront = "";
    private String tactileHeadTextMiddle = "";
    private String tactileHeadTextRear = "";


    public boolean establishConnection(String url){
        this.url = url; // set ClassVar url to url, so NAO_Connection can access it.
        ExecutorService executor = Executors.newSingleThreadExecutor(); // Create executor for the timeout
        Future<Session> future = executor.submit(new NAO_Connection()); // Create Thread for Connectionestablishment

        try {                                               // try to establish the connection
            session = future.get(10, TimeUnit.SECONDS); // set session to hold the connection
        } catch (Exception e) {
            future.cancel(true);                        // kill the second Thread
            executor.shutdownNow();                     // shut down the executor
            return false;
        }

        if (session != null && session.isConnected()) {
            try {
                motion = new ALMotion(session);         // Create the NAO Control Objects, if the
                tts = new ALTextToSpeech(session);      // connection succeeds.
                pose = new ALRobotPosture(session);
                led = new ALLeds(session);
                bat = new ALBattery(session);
                temp = new ALBodyTemperature(session);
                memory = new ALMemory(session);
                play = new ALAudioPlayer(session);
                audioDevice = new ALAudioDevice(session);
                subscribeToTactileHead();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void closeConnection() {
        if (session != null && !session.isConnected()) {
            session.close();
            session = null;
        }
    }

    public void checkConnection() throws ConnectionException {
        if (session != null && !session.isConnected()) {
            throw new ConnectionException();
        }
    }


    public void moveHead(float left, float right, float down, float up) throws ConnectionException { // unfertig #testing
        checkConnection();
        try {
            motion.angleInterpolation("HeadYaw", left - right, 0.1f, false); // move Head left or right
            motion.angleInterpolation("HeadPitch", down - up, 0.1f, false); // move Head up or down
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveHead(String direction) throws ConnectionException {
        checkConnection();
        float strongness = 0.05f; // how much does the head move per call
        String joint; // Moving horizontal ("HeadPitch") or vertical ("HeadYaw")
        float move; // moving up/right (negative value) or down/left (positive value)

        switch (direction) { // set variables for the correct movement
            case "up":
                move = -1 * strongness;
                joint = "HeadPitch";
                break;
            case "down":
                move = strongness;
                joint = "HeadPitch";
                break;
            case "right":
                move = -1 * strongness;
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
        } catch (Exception e) {
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
        checkConnection();                                          // not used, apparently it doesn't change
    try {                                                           // anything, if the language is changed.
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
        } catch (Exception e) {
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

    public void moveToward(float x, float y, float thata, float v) throws ConnectionException {
        checkConnection(); //OLD Method not used anymore

        x = x * v;
        y = y * v;
        thata = thata * v;

        try {
            motion.moveToward(x, y, thata);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMoveX(float x) throws ConnectionException { // set the x direction of motion and
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

    public void setMoveY(float y) throws ConnectionException {// set the y direction of motion and
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

    public void setMoveT(float t) throws ConnectionException {
        checkConnection();

        moveT = t; // NAO can spin and walk in one direction at once, so just let him spin.

        try {
            // set the new movement directions combined with the speed.
            motion.moveToward(moveX * moveV, moveY * moveV, moveT * moveV);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMoveV(float v) throws ConnectionException { // sets the speed of the movement
        checkConnection();
        moveV = v;

        try {
            // set the new speed combined with the movement directions
            motion.moveToward(moveX * moveV, moveY * moveV, moveT * moveV);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void moveArm(String joint, float direction) throws ConnectionException {
        checkConnection();
        try {
            motion.angleInterpolation(joint, 0.1f * direction, 0.1f, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveArm(String jointr, String jointl, float direction) throws ConnectionException {
        checkConnection();
        try {
            motion.angleInterpolation(jointr, 0.1f * direction, 0.1f, false);
            motion.angleInterpolation(jointl, 0.1f * direction, 0.1f, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void changeEyeColor(String eye, float red, float green, float blue) throws ConnectionException {
        checkConnection();

        try {
            if (eye == "Right") {
                led.fadeRGB("RightFaceLeds", red, green, blue, 0f);
            } else if (eye == "Left") {
                led.fadeRGB("LeftFaceLeds", red, green, blue, 0f);
            } else if (eye == "Both") {
                led.fadeRGB("RightFaceLeds", red, green, blue, 0f);
                led.fadeRGB("LeftFaceLeds", red, green, blue, 0f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeEyeColor(String eye, Color color) throws ConnectionException {
        checkConnection();
        float red = (float) color.getRed();
        float green = (float) color.getGreen();
        float blue = (float) color.getBlue();

        this.changeEyeColor(eye, red, green, blue);
    }

    public double batteryPercent() throws InterruptedException {       //Get the battery charge in percents

        try {
            return bat.getBatteryCharge();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public float getTemp() throws ConnectionException { //get the tempreture from NAO
        checkConnection();                              // returns -1/0/1 for tempdiagnosis, -2 for no temperature available

        try {
            Object temp1 = temp.getTemperatureDiagnosis();

            if (temp1 instanceof ArrayList) {
                ArrayList tempList = (ArrayList) temp1;
                Object temp2 = tempList.get(0);
                return Float.parseFloat(temp2.toString());
            } else {
                return -2f;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2f;
    }


    public List<String> getSoundFiles() throws ConnectionException { // return list of installed soundfiles.
        checkConnection();

        try {
            return play.getSoundSetFileNames("Aldebaran"); // try to get soundfiles
        }catch (CallError e){ // return null, if there aren't soundfiles.
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean switchRest() throws ConnectionException { //Switch between rest and wakeUp
        checkConnection();                                   // wakes up if rest and otherwise

        try {
            if (!motion.robotIsWakeUp()) {
                motion.wakeUp();
            } else {
                motion.rest();
            }
            return motion.robotIsWakeUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void playSound(String sound) throws ConnectionException { //plays given Soundfile
        checkConnection();

        try {
            play.playSoundSetFile("Aldebaran", sound);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setTactileHeadTextFront(String front){
        tactileHeadTextFront = front;
    }

    public void setTactileHeadTextMiddle(String middle){
        tactileHeadTextMiddle = middle;
    }

    public void setTactileHeadTextRear(String rear){
        tactileHeadTextRear = rear;
    }

    public void subscribeToTactileHead() throws ConnectionException {   // set NAO to say a given text if his head is touched
        checkConnection();                                              // This method is called after the connection succeeded.
        try {
            memory.subscribeToEvent(
                    "FrontTactilTouched", new EventCallback<Float>() {  // set the front sensor to speak the variable "tactileHeadFront"
                        @Override                                       // "tactileHeadFront" is rewritten from the GUI, if the textfield has been changed.
                        public void onEvent(Float arg0)
                                throws InterruptedException, CallError {
                            // 1 means the sensor has been pressed
                            if (arg0 > 0) {
                                tts.say(tactileHeadTextFront);
                            }
                        }
                    });
            memory.subscribeToEvent(
                    "MiddleTactilTouched", new EventCallback<Float>() { // see explanation above
                        @Override
                        public void onEvent(Float arg0)
                                throws InterruptedException, CallError {
                            // 1 means the sensor has been pressed
                            if (arg0 > 0) {
                                tts.say(tactileHeadTextMiddle);
                            }
                        }
                    });
            memory.subscribeToEvent(
                    "RearTactilTouched", new EventCallback<Float>() {   // see explanation above
                        @Override
                        public void onEvent(Float arg0)
                                throws InterruptedException, CallError {
                            // 1 means the sensor has been pressed
                            if (arg0 > 0) {
                                tts.say(tactileHeadTextRear);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVolume(int vol) throws ConnectionException{
        checkConnection();

        try {
            audioDevice.setOutputVolume(vol);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

