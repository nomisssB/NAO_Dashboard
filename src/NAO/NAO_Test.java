package NAO;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        nao.establishConnection("tcp://127.0.0.1:59060;");

        //nao.execPosture("crouch");

        nao.moveToward(1f, 0f,0f,0.2f);
        Thread.sleep(2000);
        nao.moveToward(-1f, 0f ,0f,1f);    Thread.sleep(2000);
        nao.moveToward(0f, 0f ,0f,0f);


    }
}
