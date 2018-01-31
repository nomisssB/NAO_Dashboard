package GUI;

import javax.management.OperationsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfiguratorTest {

    public static void main(String[] args) {
        List<String> connect1= new ArrayList<String>();

        Configurator.loader();
        connect1.add("test");
        String[] items = Configurator.props.get("urls").toString().split("\\,");
        connect1 = Arrays.asList(items);
        List<String> itemList = new ArrayList<String>(Arrays.asList(items));
        itemList.add("fox");
        connect1.add("test1");

        String connectionsList = String.join(",",connect1);
        Configurator.saver("urls",connectionsList);
        }

}
