package NAO;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.proxies.ALVideoDevice;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import naoDash_main.Controller;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;

public class NAO_Cam extends Thread{
    ALVideoDevice videoDevice;
    int camera = 0;


    public NAO_Cam(ALVideoDevice videoDevice){
        setDaemon(true);
        setName("CamThread");
        this.videoDevice = videoDevice;
    }

    public void run() {

        while (!this.isInterrupted()) {


            try {
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
                Image image = SwingFXUtils.toFXImage(img, null);
                // UI updaten
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        // entsprechende UI Komponente updaten TODO
                        System.out.println("update image...");
                        Controller.updateImage(image);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }




            // Thread schlafen
            try {

                sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }


    }







}
