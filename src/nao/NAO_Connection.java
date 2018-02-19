package nao;

import com.aldebaran.qi.Session;

import java.util.concurrent.Callable;

public class NAO_Connection implements Callable<Session>{

    public Session call() {
        try {
            Session session = new Session(NAO.url);
            return session;
        } catch (Exception e) {
            return null;
        }

    }

}
