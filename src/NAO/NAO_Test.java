package NAO;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        nao.establishConnection("tcp://127.0.0.1:40261;");


        nao.getVoices();









    }
}
