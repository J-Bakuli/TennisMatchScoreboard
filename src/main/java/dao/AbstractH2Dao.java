package dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import util.HibernateUtil;

@Slf4j
public class AbstractH2Dao {
    protected Session session() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }

    protected boolean isDuplicate(Exception e) {
        Throwable t = e;
        while (t != null) {
            if (t instanceof ConstraintViolationException) {
                return true;
            }
            t = t.getCause();
        }
        return false;
    }
}
