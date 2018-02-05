package NAO;

import com.aldebaran.qi.Callback;
import com.aldebaran.qi.Session;

import java.util.concurrent.Callable;

public class NAO_Connection implements Callable<Session>{
    private static Session session;

    public Session call() {
        //Session session = new Session();
        try {
            //session.connect(NAO.url).get();
            Session session = new Session(NAO.url);
            return session;
        } catch (Exception e) {
            return null;
        }

    }

}
