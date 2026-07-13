package util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {
    private final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("SessionFactory init failed: " + ex.getMessage());
        }
    }

    public SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public void shutdown() {
        if (!SESSION_FACTORY.isClosed()) {
            getSessionFactory().close();
        }
    }
}
