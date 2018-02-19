package nao;


import com.aldebaran.qi.helper.proxies.ALVideoDevice;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;

public class NAO_Cam extends Thread{
    //Variable Declarations
    ALVideoDevice videoDevice;
    ImageView imageView;
    int camera = 0;

    public NAO_Cam(ALVideoDevice videoDevice, ImageView imageView){
        setDaemon(true);
        setName("CamThread");
        this.videoDevice = videoDevice;
        this.imageView = imageView;
    }

    public void run() {

        while (!this.isInterrupted()) {

            try {
                String pic_nr = "" + System.nanoTime();
                String subscriber = videoDevice.subscribeCamera(pic_nr, camera, 1, 11, 10);             // Subscriber for every single picture
                List<Object> video_container = (List<Object>) videoDevice.getImageRemote(subscriber);   // Container for image data
                videoDevice.releaseImage(pic_nr);
                videoDevice.unsubscribe(pic_nr);
                /* NAO-Java-Doc for Object in video_container:
                 * Array containing image informations : [0] : width; [1] : height; [2] : number of layers; [3] : ColorSpace;
                 * [4] : time stamp (highest 32 bits); [5] : time stamp (lowest 32 bits);
                 * [6] : array of size height * width * nblayers containing image data; [7] : cameraID;
                 * [8] : left angle; [9] : top angle; [10] : right angle; [11] : bottom angle;
                 */
                ByteBuffer buffer = (ByteBuffer) video_container.get(6); //image data is on index 6 in container

                // Create byte-array out of image data
                byte[] binaryImage = buffer.array();

                // Convert byte array to an int array
                int[] intArray;
                intArray = new int[320 * 240];                  // Declare int array for every pixel (32bit int -> 8 bit alpha + 3 * 8 bit for red/green/blue)
                for (int i = 0; i < 320 * 240; i++) {           // Write pixel-values to the int array
                    intArray[i] = ((255 & 0xFF) << 24) |        // alpha for every pixel is set to max (FF)
                            // red / green / blue values are extracted from the binary image and shifted to the right spot in the corresponding int value
                            // red -> bit 16-23 / green -> bit 8-15 / blue -> bit 0-7
                            ((binaryImage[i * 3 + 0] & 0xFF) << 16) |   // red
                            ((binaryImage[i * 3 + 1] & 0xFF) << 8) |    // green
                            ((binaryImage[i * 3 + 2] & 0xFF) << 0);     // blue
                            // finally all values are merged via OR operators, so we have one integer per pixel.
                }

                // Creation of a BufferedImage, which is set to hold the image represented by the int array.
                BufferedImage img = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);
                img.setRGB(0, 0, 320, 240, intArray, 0, 320);
                // Creation of a JavaFX Image out of the buffered Image.
                Image image = SwingFXUtils.toFXImage(img, null);

                // Update the UI, as soon if it's possible.
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImage(image);
                    }
                });

            } catch (Exception e) {

            }

            // Sleep for half a second...
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                this.interrupt();
            }

        }
    }







}
