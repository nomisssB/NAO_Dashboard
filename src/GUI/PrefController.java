package GUI;
/*
FILE: PrefController.java
USAGE: CURRENTLY NOT IN USE. Planned to use as Preferences-window...
 */
import javafx.event.ActionEvent;
import naoDash_main.Controller;

public class PrefController {
    public void quit(ActionEvent actionEvent) {
        Controller.prefs.close();
    }
}
