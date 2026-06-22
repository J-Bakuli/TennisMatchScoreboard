package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import util.HibernateUtil;

public class H2PlayerDaoTest extends AbstractPlayerDaoTest {
    private Session session;

    @BeforeEach
    public void setUp() throws Exception {
        playerDao = new H2PlayerDao();

        try (Session cleanupSession = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = cleanupSession.beginTransaction();
            cleanupSession.createMutationQuery("delete from FinishedMatchEntity").executeUpdate();
            cleanupSession.createMutationQuery("delete from PlayerEntity").executeUpdate();
            tx.commit();
        }

        session = HibernateUtil.getSessionFactory().openSession();
        ManagedSessionContext.bind(session);
        session.beginTransaction();
    }

    @AfterEach
    public void end() {
        Transaction tx = session.getTransaction();
        if (tx.isActive()) {
            tx.rollback();
        }
        ManagedSessionContext.unbind(session.getSessionFactory());
        session.close();
    }
}
