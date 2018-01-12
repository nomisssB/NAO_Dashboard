package GUI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParse {

    private Pattern patternIP;
    private Pattern patternPort;
    private Matcher matcher;

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final String TCPPORT_PATTERN =
            "^(6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3})$";

   public  InputParse() {
       patternIP = Pattern.compile(IPADDRESS_PATTERN);
       patternPort = Pattern.compile(TCPPORT_PATTERN);
   }

    public boolean validateIP(final String ip){
        matcher = patternIP.matcher(ip);
        return matcher.matches();
    }

    public boolean validatePort(final String port){
        matcher = patternPort.matcher(port);
        return matcher.matches();
    }

}
