package NAO;

public class NAO_Test {
    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();

        nao.establishConnection("tcp://192.168.1.146:9559;");

        //nao.execPosture("crouch");

        /*nao.moveToward(1f, 0f,0f,0.2f);
        Thread.sleep(2000);
        nao.moveToward(-1f, 0f ,0f,1f);    Thread.sleep(2000);
        nao.moveToward(0f, 0f ,0f,0f);*/

        /*nao.changeEyeColor("Both", 0f,0f,1f);
        Thread.sleep(2000);
        nao.changeEyeColor("Right", 0f,1f,0f);
        nao.changeEyeColor("Left", 1f,0f,0f);
        Thread.sleep(2000);
        nao.changeEyeColor("Both", 1f,0f,1f);*/


        System.out.println(nao.batteryPercent());

    }
}
