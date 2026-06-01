package controller;

import dao.MatchesDao;
import dao.PlayerDao;
import db.AppLifecycleListener;
import dto.FinishedMatchDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import service.FinishedMatchesService;

import java.io.IOException;
import java.util.List;

@WebServlet({"/matches"})
@Slf4j
public class MatchesServlet extends BaseServlet {
    private FinishedMatchesService finishedMatchesService;

    @Override
    public void init() throws ServletException {
        MatchesDao matchesDao = getRequiredAttribute(AppLifecycleListener.MATCHES_DAO_ATTR, MatchesDao.class);
        PlayerDao playerDao = getRequiredAttribute(AppLifecycleListener.PLAYER_DAO_ATTR, PlayerDao.class);
        finishedMatchesService = new FinishedMatchesService(matchesDao, playerDao);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /matches");
        List<FinishedMatchDto> finishedMatches = finishedMatchesService.getAllFinishedMatches();
        req.setAttribute("matches", finishedMatches);
        req.getRequestDispatcher("/WEB-INF/views/matches.jsp").forward(req, resp);
    }
}
