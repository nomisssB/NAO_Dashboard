package NAO;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        System.out.println(nao.establishConnection("tcp://192.168.1.127:9559;"));
        System.out.println(nao.getSoundFiles());
        System.out.println(nao.establishConnection("tcp://192.168.1.136:9559;"));
        System.out.println(nao.getSoundFiles());














    }
}
