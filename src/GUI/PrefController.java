package GUI;

import javafx.event.ActionEvent;
import naoDash_main.Controller;

public class PrefController {
    public void quit(ActionEvent actionEvent) {
        Controller.prefs.close();
    }
}
