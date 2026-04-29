package db;

import dao.InMemoryOngoingMatchDao;
import dao.OngoingMatchDao;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

@Slf4j
@WebListener
public class AppLifecycleListener implements ServletContextListener {
    public static final String ONGOING_MATCH_DAO_ATTR = "ongoingMatchDao";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Application context initialization started");
        try {
            SessionFactory ignored = HibernateUtil.getSessionFactory();
            OngoingMatchDao ongoingMatchDao = new InMemoryOngoingMatchDao();
            sce.getServletContext().setAttribute(ONGOING_MATCH_DAO_ATTR, ongoingMatchDao);
            log.info("Hibernate SessionFactory initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize Hibernate SessionFactory", e);
            throw e;
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Application context destroy started, closing SessionFactory");
        try {
            HibernateUtil.shutdown();
            log.info("Hibernate SessionFactory closed");
        } catch (Exception e) {
            log.error("Failed to close Hibernate SessionFactory", e);
        }
    }
}
