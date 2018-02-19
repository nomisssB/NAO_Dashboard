//package GUI;
/*
FILE: _notUsedCode.java
USAGE: CURRENTLY NOT IN USE. May be used for parallel tasks during runtime...
 */

/*import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.aldebaran.qi.CallError;
import naoDash_main.Controller;
import NAO.NAO;*/


/*public class Timers {

    private static Timer timer_Bat;

    public static void battery_timer(NAO nao){
        timer_Bat = new Timer("NaoDash_BatteryTimer", true);
        timer_Bat.schedule(new BatteryTask(nao),1000,5000);
    }
    //Temperature_Timer method

    public static void killBatTimer(){
        timer_Bat.cancel();
    }
}

class BatteryTask extends TimerTask
{
    private NAO nao;
    BatteryTask(NAO nao){
        this.nao = nao;
}
    @Override public void run()
    {
        try {
            Controller.batteryV = nao.getBatteryPercent();
            System.out.println(Controller.batteryV);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}*/
/*
    public void getImage(){
        try {
            String camera = "kamera";
            System.out.println("subscribers "+videoDevice.getSubscribers());
            System.out.println("active camera " +videoDevice.getActiveCamera());
            System.out.println("camera indexes " +videoDevice.getCameraIndexes());
            System.out.println("cameramodel 0 "+ videoDevice.getCameraModel(0));
            System.out.println("cameramodel 1 "+ videoDevice.getCameraModel(1));
            System.out.println("name 0" + videoDevice.getCameraName(0));
            System.out.println("name 1"+ videoDevice.getCameraName(1));
            System.out.println("brokername "+videoDevice.getBrokerName());
            System.out.println("open 1 "+videoDevice.isCameraOpen(1));
            System.out.println("open 0 "+videoDevice.isCameraOpen(0));
            System.out.println("resolution " + videoDevice.getResolution(0));
            System.out.println("fps " + videoDevice.getFrameRate(0));


            videoDevice.unsubscribeAllInstances(camera);
            videoDevice.subscribeCamera(camera,0,videoDevice.getResolution(0),videoDevice.getColorSpace(0),videoDevice.getFrameRate(0));
            System.out.println(videoDevice.getImageRemote(camera));
            List<Object> imageContainer = (List<Object>) videoDevice.getImageRemote(camera);
            //ByteBuffer buffer = (ByteBuffer)imageContainer.get(6);
            //byte[] imageRawData = buffer.array(); // your image is contained here
            videoDevice.unsubscribe(camera); // don't forget to unsubscribe
            System.out.println();
        } catch (CallError callError) {
            callError.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }*/
