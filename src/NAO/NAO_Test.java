package NAO;

import javafx.scene.paint.Color;

import java.net.InetAddress;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        System.out.println(nao.establishConnection("tcp://192.168.1.146:9559;"));
        nao.sayText("test");
        nao.getCameraStream(0);
        nao.getImage();







    }
}
