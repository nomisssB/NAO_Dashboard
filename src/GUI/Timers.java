package GUI;

import java.util.Timer;
import java.util.TimerTask;
import naoDash_main.Controller;
import NAO.NAO;

public class Timers {

    private static Timer timer_Bat;

    public static void battery_timer(NAO nao){
        timer_Bat = new Timer("NaoDash_BatteryTimer", true);
        timer_Bat.schedule(new BatteryTask(nao),1000,5000);
    }

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
            Controller.batteryV = nao.batteryPercent();
            System.out.println(Controller.batteryV);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}