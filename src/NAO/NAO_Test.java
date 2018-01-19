package NAO;

import java.util.ArrayList;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        //nao.establishConnection("tcp://127.0.0.1:40261;");
        nao.establishConnection("tcp://192.168.1.127:9559;");


        //nao.execPosture("Stand");

        nao.setMoveV(1f);
        nao.setMoveX(1f);

        Thread.sleep(5000);

        nao.setMoveV(0.5f);

        Thread.sleep(5000);

        nao.setMoveV(1f);


        Thread.sleep(5000);


        nao.setMoveX(0f);
        nao.setMoveT(0f);










    }
}
