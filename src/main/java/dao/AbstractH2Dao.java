package dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

@Slf4j
public class AbstractH2Dao {
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

    protected void rollbackSafely(Transaction tx, Exception originalError) {
        if (tx == null || !tx.isActive()) {
            return;
        }
        try {
            tx.rollback();
        } catch (Exception rollbackError) {
            log.warn("Rollback failed after original error: {}", originalError.getMessage(), rollbackError);
        }
    }
}
