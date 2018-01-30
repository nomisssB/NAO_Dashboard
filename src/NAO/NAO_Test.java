package NAO;

import javafx.scene.paint.Color;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        System.out.println(nao.establishConnection("tcp://192.168.1.148:9559;"));











        System.out.println(nao.batteryPercent());









    }
}
