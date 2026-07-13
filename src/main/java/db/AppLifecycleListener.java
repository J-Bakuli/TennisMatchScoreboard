package db;

import dao.H2MatchesDao;
import dao.H2PlayerDao;
import dao.InMemoryOngoingMatchDao;
import dao.MatchesDao;
import dao.OngoingMatchDao;
import dao.PlayerDao;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import service.FinishedMatchesService;
import service.MatchScoreCalculationService;
import service.NewMatchService;
import service.OngoingMatchService;
import util.HibernateUtil;

@Slf4j
@WebListener
public class AppLifecycleListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Application context initialization started");
        try {
            SessionFactory ignored = HibernateUtil.getSessionFactory();
            OngoingMatchDao ongoingMatchDao = new InMemoryOngoingMatchDao();
            PlayerDao playerDao = new H2PlayerDao();
            MatchesDao matchesDao = new H2MatchesDao();
            MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
            FinishedMatchesService finishedMatchesService = new FinishedMatchesService(matchesDao);
            NewMatchService newMatchService = new NewMatchService(playerDao, ongoingMatchDao);
            OngoingMatchService ongoingMatchService = new OngoingMatchService(
                    matchScoreCalculationService, finishedMatchesService, ongoingMatchDao, playerDao);

            sce.getServletContext().setAttribute(OngoingMatchDao.class.getSimpleName(), ongoingMatchDao);
            sce.getServletContext().setAttribute(PlayerDao.class.getSimpleName(), playerDao);
            sce.getServletContext().setAttribute(MatchesDao.class.getSimpleName(), matchesDao);
            sce.getServletContext().setAttribute(NewMatchService.class.getSimpleName(), newMatchService);
            sce.getServletContext().setAttribute(OngoingMatchService.class.getSimpleName(), ongoingMatchService);
            sce.getServletContext().setAttribute(FinishedMatchesService.class.getSimpleName(), finishedMatchesService);
            log.info("Hibernate SessionFactory, DAO and services initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize application components", e);
            throw e;
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Application context destroy started, closing SessionFactory");
        try {
            HibernateUtil.shutdown();
            log.info("Hibernate SessionFactory is closed");
        } catch (Exception e) {
            log.error("Failed to close Hibernate SessionFactory", e);
        }
    }
}
