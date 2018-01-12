package NAO;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        nao.establishConnection("tcp://127.0.0.1:36723;");


        nao.sayText("test1");

        Thread.sleep(500);

        nao.establishConnection("tcp://127.0.0.1:43207;");

        nao.sayText("teste2");




    }
}
