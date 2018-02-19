package nao;

import com.aldebaran.qi.Session;
import java.util.concurrent.Callable;

public class NAO_Connection implements Callable<Session>{

    public Session call() {
        try {
            // Try to create a new session and return it, if successful
            Session session = new Session(NAO.url);
            return session;
        } catch (Exception e) {
            return null;
        }

    }

}
