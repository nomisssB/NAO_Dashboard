package NAO;

public class NAO_Test {
    public static void main(String[] args) {
        NAO nao = new NAO();

        System.out.println(nao.establishConnection("tcp://127.0.0.1:36565;"));
        System.out.println(nao.establishConnection("tcp://127.0.0.1:36565;"));
    }
}
