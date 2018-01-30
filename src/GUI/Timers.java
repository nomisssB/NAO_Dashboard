package GUI;

import java.util.Timer;
import java.util.TimerTask;
import naoDash_main.Controller;
import NAO.NAO;


/*This class isn't in use currently...*/

public class Timers {

    private static Timer timer_Bat;
    private static Timer timer_Temp;

    public static void battery_timer(NAO nao){
        timer_Bat = new Timer("NaoDash_BatteryTimer", true);
        timer_Bat.schedule(new BatteryTask(nao),1000,5000);
    }
    //Temperature_Timer method
    public static void temperature_timer(NAO nao){
        timer_Temp = new Timer("NaoDash_TemperatureTimer", true);
        timer_Temp.schedule(new TemperatureTask(nao),1000,5000);
    }
    public static void killBatTimer(){
        timer_Bat.cancel();
    }
    public static void killTemperatureTimer(){
        timer_Temp.cancel();
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
//        try {
//            Controller.batteryV = nao.batteryPercent();
//            System.out.println(Controller.batteryV);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}

class TemperatureTask extends TimerTask {
    private NAO nao;

    TemperatureTask(NAO nao) {
        this.nao = nao;
    }

    @Override
    public void run() {
    }
}
