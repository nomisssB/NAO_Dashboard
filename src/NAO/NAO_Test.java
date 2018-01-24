package NAO;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        nao.establishConnection("tcp://192.168.1.128:9559;");


        nao.getSoundFiles();
        nao.playSound("");









    }
}
