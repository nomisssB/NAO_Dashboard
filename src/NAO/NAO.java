package NAO;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import naoDash_main.Controller;

import static GUI.LoginController.nao1;

public class NAO {
    //Nao Control stuff declarations
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
    private static ALVideoDevice videoDevice;

    //Variable Declarations
    private static float moveX; // movement forwards / backwards
    private static float moveY; // movement sideways
    private static float moveT; // movement spinning (T = Theta)
    private static float moveV; // velocity of movement
    public static String url;
    private String tactileHeadTextFront = "";
    private String tactileHeadTextMiddle = "";
    private String tactileHeadTextRear = "";



    public boolean establishConnection(String url){ // returns true if a connection has been established.
        // To implement a connectiontimeout, the main connection establishment process runs in an own time limited thread.
        // Therefore a class NAO_Connection has been implemented, which just establishes the connection and returns the session object.
        this.url = url; // set ClassVar url to url, so NAO_Connection can access it.
        ExecutorService executor = Executors.newSingleThreadExecutor(); // Create executor for the timeout
        Future<Session> future = executor.submit(new NAO_Connection()); // Create Thread for the connectionestablishment

        try {                                                           // try to establish the connection
            session = future.get(10, TimeUnit.SECONDS);                 // set session to hold the generated session object.
        } catch (Exception e) {
            future.cancel(true);                                        // kill the second Thread, if something went wrong
            executor.shutdownNow();                                     // and shut down the executor.
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
                videoDevice = new ALVideoDevice(session);
                subscribeToTactileHead();               // activate the subscriptions of the head sensors.
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void closeConnection() { // Closes the connection to the Nao
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

    public boolean checkConnection(boolean b) {
        if (session != null && !session.isConnected()) {
            return false;
        }
        return true;
    }

    public void moveHead(String direction) throws ConnectionException { // Moves Nao's head a little bit into a given direction.
        checkConnection();
        float strongness = 0.01f;   // how much does the head move per call
        String joint;               // Moving horizontal ("HeadPitch") or vertical ("HeadYaw")
        float move;                 // moving up/right (negative value) or down/left (positive value)

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

    public void sayText(String text) throws ConnectionException { // old sayText with only text.
        checkConnection();
        try {
            tts.say(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sayText(String text, String voice, float pitch) throws ConnectionException { // say a given text, with a given voice and pitch.
        checkConnection();
        try {
            tts.setVoice(voice);
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

    public void moveToward(float x, float y, float thata, float v) throws ConnectionException { // old move toward.
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


    // --------- Walking Section ---------
    // There are 4 methods to let the Nao walk. (one for each movement direction (x / y / t) and one to set the speed)
    // The value for each direction is also stored in a variable, because we can't just set one value.
    // If one method is called, the corresponding value is changed
    // and the "moveToward" method of the aldebaran library is recalled with the new value(s).
    // The nao will walk, until the values are changed again. So each time a button is pressed or released, one of the methods will be called.
    // Because nao can't move in x and y direction at once, this case is intercepted.
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
            motion.setMoveArmsEnabled(true,true);
            motion.moveToward(moveX * moveV, moveY * moveV, moveT * moveV);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // -------------------------------------

    public void moveArm(String joint, float direction) throws ConnectionException { // Moves the joint "joint" a little bit in the direction "direction" (direction = 1 / -1)
        checkConnection();                                                          // Which joint and direction needs to be called is determined in the Controller Class.
        try {
            motion.angleInterpolation(joint, 0.1f * direction, 0.01f, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeEyeColor(String eye, float red, float green, float blue) throws ConnectionException { // Changes the Color of "eye" to the values "red", "blue", "green"
        checkConnection();                                                                                  // eye = "Right" / "Left" / "Both"

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

    public void changeEyeColor(String eye, Color color) throws ConnectionException {// Changes the Color of "eye" to the Color "color".
        checkConnection();                                                          // eye = "Right" / "Left" / "Both"
        float red = (float) color.getRed();
        float green = (float) color.getGreen();
        float blue = (float) color.getBlue();

        this.changeEyeColor(eye, red, green, blue);
    }

    public double batteryPercent() throws ConnectionException {       //Get the battery charge in percents
        checkConnection();

        try {
            return bat.getBatteryCharge();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public float getTemp() throws ConnectionException { //get the tempreture from NAO
        checkConnection();                              // returns 0/1/2 for tempdiagnosis, -1 for no temperature available

        try {
            Object temp1 = temp.getTemperatureDiagnosis();

            if (temp1 instanceof ArrayList) {
                ArrayList tempList = (ArrayList) temp1;
                Object temp2 = tempList.get(0);
                return Float.parseFloat(temp2.toString());
            } else {
                return -1f;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1f;
    }


    public List<String> getSoundFiles() throws ConnectionException { // return list of installed soundfiles or null if there aren't any soundfiles installed.
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

    public void switchRest() throws ConnectionException { //Switch between rest and wakeUp
        checkConnection();                                   // wakes up if rest and otherwise

        try {
            if (!motion.robotIsWakeUp()) {
                motion.wakeUp();
            } else {
                motion.rest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSound(String sound) throws ConnectionException { //plays a given soundfile of the soundset "Aldebaran"
        checkConnection();

        try {
            play.playSoundSetFile("Aldebaran", sound);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setTactileHeadTextFront(String front){      // sets the text, which will be said, if one of the head sensors is touched.
        tactileHeadTextFront = front;
    }

    public void setTactileHeadTextMiddle(String middle){    // sets the text, which will be said, if one of the head sensors is touched.
        tactileHeadTextMiddle = middle;
    }

    public void setTactileHeadTextRear(String rear){        // sets the text, which will be said, if one of the head sensors is touched.
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
            e.printStackTrace("RearTactilTouched", );
        }
    }

    public void setVolume(int vol) throws ConnectionException{      // sets the Volume with which the Nao speaks.
        checkConnection();

        try {
            audioDevice.setOutputVolume(vol);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Image getCameraStream(int camera) throws Exception {

        String pic_nr = "" + System.nanoTime();
        String subscriber = videoDevice.subscribeCamera(pic_nr, camera, 1, 11,
                10); //Subscriber for every single picture
        List<Object> video_container = (List<Object>) videoDevice
                .getImageRemote(subscriber); //Container for image data
        ByteBuffer buffer = (ByteBuffer) video_container.get(6); //image data is on index 6 in container

        /*NAO-Java-Doc:
         Array containing image informations : [0] : width; [1] : height; [2] : number of layers; [3] : ColorSpace;
         [4] : time stamp (highest 32 bits); [5] : time stamp (lowest 32 bits);
         [6] : array of size height * width * nblayers containing image data; [7] : cameraID;
         [8] : left angle; [9] : top angle; [10] : right angle; [11] : bottom angle;
         https://github.com/huberpa/NAO-humanoid-robot/blob/master/Nao/src/BilderThread.java
         */


        byte[] binaryImage = buffer.array(); //create byte-array out of image data
        videoDevice.releaseImage(pic_nr);
        videoDevice.unsubscribe(pic_nr);
        int[] intArray;
        intArray = new int[320 * 240]; //int array for every pixel
        for (int i = 0; i < 320 * 240; i++) { //write pixel-values in intarray
            intArray[i] = ((255 & 0xFF) << 24) | // alpha
                    ((binaryImage[i * 3 + 0] & 0xFF) << 16) | // red
                    ((binaryImage[i * 3 + 1] & 0xFF) << 8) | // green
                    ((binaryImage[i * 3 + 2] & 0xFF) << 0); // blue
        }

        BufferedImage img = new BufferedImage(320, 240,
                BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 320, 240, intArray, 0, 320);

        return SwingFXUtils.toFXImage(img, null); //convert to an "image"-object for displaying in imageView
    }


}

