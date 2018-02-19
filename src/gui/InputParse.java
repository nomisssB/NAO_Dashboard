package gui;
/*
FILE: InputParse.java
USAGE: Used for parsing strings on regular expressions
 */
import java.util.regex.Pattern;

public class InputParse {

    private Pattern patternIP, patternPort;

    private static final String IPADDRESS_PATTERN = //Pattern for parsing an ip-address
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + //first part XXX.xxx.xxx.xxx, digits between 0 and 255
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + //second part xxx.XXX.xxx.xxx, digits between 0 and 255
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + //third part xxx.xxx.XXX.xxx, digits between 0 and 255
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"; //fourth part xxx.xxx.xxx.XXX, digits between 0 and 255
    private static final String TCPPORT_PATTERN = //Pattern for parsing a port. Digits between 1 and 65336
            "^(6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3})$";

   InputParse() {
       patternIP = Pattern.compile(IPADDRESS_PATTERN); //"convert" RegEx-String to pattern
       patternPort = Pattern.compile(TCPPORT_PATTERN);
   }

    public boolean validateIP(String ip){ //returns whether parameter fits to RegEx
        return patternIP.matcher(ip).matches();
    }

    public boolean validatePort(String port){
        return patternPort.matcher(port).matches();
    }

}
