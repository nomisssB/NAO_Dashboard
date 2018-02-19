package gui;
/*
FILE: PrefController.java
USAGE: CURRENTLY NOT IN USE. Planned to use as Preferences-window...
 */
import javafx.event.ActionEvent;

public class PrefController {
    public void quit(ActionEvent actionEvent) {
        Controller.prefs.close();
    }
}
