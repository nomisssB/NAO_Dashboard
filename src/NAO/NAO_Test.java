package NAO;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        nao.establishConnection("tcp://192.168.1.127:9559;");



        nao.playSound("amb_angry_crowd");









    }
}
