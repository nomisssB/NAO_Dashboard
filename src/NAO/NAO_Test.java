package NAO;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        nao.establishConnection("tcp://192.168.1.133:9559;");

        nao.sayText("test1");

        nao.establishConnection("tcp://192.168.1.133:9559;");

        nao.sayText("test2");


    nao.batteryPercent();










    }
}
