package gui;
/*
* FILE: InputHelper.java
* USAGE: Helper methods for parsing strings on regular expressions
*/
import java.util.regex.Pattern;

public class InputHelper {
    // Variables Declaration
    private Pattern patternIP, patternPort;
    private static final String regexIpAddress = // Pattern for parsing an ip-address
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + // first part XXX.xxx.xxx.xxx, digits between 0 and 255
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + // second part xxx.XXX.xxx.xxx, digits between 0 and 255
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + // third part xxx.xxx.XXX.xxx, digits between 0 and 255
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"; // fourth part xxx.xxx.xxx.XXX, digits between 0 and 255
    private static final String regexTcpPort = // Pattern for parsing a port. Digits between 1 and 65336
            "^(6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3})$";

   InputHelper() {
       // create patterns from RegEx-strings on constructor call
       patternIP = Pattern.compile(regexIpAddress);
       patternPort = Pattern.compile(regexTcpPort);
   }

    public boolean validateIP(String ip){ // returns whether "ip" matches IP-regex or not
        return patternIP.matcher(ip).matches();
    }

    public boolean validatePort(String port){ // returns whether "port" matches TCP Port-regex or not
        return patternPort.matcher(port).matches();
    }

}
