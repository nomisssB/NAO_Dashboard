package NAO;

import javafx.scene.paint.Color;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        System.out.println(nao.establishConnection("tcp://192.168.1.127:9559;"));
        nao.test(1);
        nao.sayText("test1");
        nao.test(50);
        nao.sayText("test2");
        nao.test(100);
        nao.sayText("test3");













    }
}
