package NAO;

import java.util.ArrayList;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        //nao.establishConnection("tcp://127.0.0.1:40261;");
        nao.establishConnection("tcp://192.168.1.136:9559;");


        //nao.execPosture("Stand");

//        nao.test();

        nao.changeEyeColor("Both",0f, 1f, 0f);








    }
}
