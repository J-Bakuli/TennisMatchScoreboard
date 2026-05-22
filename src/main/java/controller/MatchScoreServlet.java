package controller;

import dao.OngoingMatchDao;
import db.AppLifecycleListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import model.OngoingMatch;
import service.OngoingMatchService;

import java.io.IOException;

@WebServlet({"/match-score"})
@Slf4j
public class MatchScoreServlet extends HttpServlet {
    private OngoingMatchService ongoingMatchService;

    @Override
    public void init() throws ServletException {
        OngoingMatchDao ongoingMatchDao = (OngoingMatchDao) getServletContext().getAttribute(AppLifecycleListener.ONGOING_MATCH_DAO_ATTR);
        if (ongoingMatchDao == null) {
            throw new ServletException("OngoingMatchDao is not initialized in ServletContext");
        }
        ongoingMatchService = new OngoingMatchService(ongoingMatchDao);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /match-score");
        String uuid = req.getParameter("uuid");

        OngoingMatch ongoingMatch = ongoingMatchService.findOngoingMatch(uuid);
        req.setAttribute("uuid", ongoingMatch.getUuid());
        req.setAttribute("matchState", ongoingMatch.getMatchState());
        req.setAttribute("player1", ongoingMatch.getPlayer1());
        req.setAttribute("player2", ongoingMatch.getPlayer2());
        req.setAttribute("winner", ongoingMatch.getWinner());
        req.getRequestDispatcher("/WEB-INF/views/match-score.jsp").forward(req, resp);
    }
}
