package controller;

import dao.MatchesDao;
import db.AppLifecycleListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import service.FinishedMatchesService;

import java.io.IOException;

@WebServlet({"/matches"})
@Slf4j
public class MatchesServlet extends BaseServlet {
    private FinishedMatchesService finishedMatchesService;

    @Override
    public void init() throws ServletException {
        MatchesDao matchesDao = getRequiredAttribute(AppLifecycleListener.MATCHES_DAO_ATTR, MatchesDao.class);
        finishedMatchesService = new FinishedMatchesService(matchesDao);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /matches");
        req.getRequestDispatcher("/WEB-INF/views/matches.jsp").forward(req, resp);
    }
}
