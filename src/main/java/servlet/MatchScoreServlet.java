package servlet;

import db.AppLifecycleListener;
import dto.MatchScoreDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import service.OngoingMatchService;
import util.ServletContextUtils;

import java.io.IOException;

@WebServlet({"/match-score"})
@Slf4j
public class MatchScoreServlet extends HttpServlet {
    private static final String MATCH_SCORE_JSP = "/WEB-INF/views/match-score.jsp";
    private OngoingMatchService ongoingMatchService;

    @Override
    public void init() throws ServletException {

        // Для получения объектов из контекста можно использовать "естественные константы" — ClassName.class.getSimpleName() или ClassName.class.getName()
        ongoingMatchService = ServletContextUtils.getRequiredAttribute(
                getServletContext(), AppLifecycleListener.ONGOING_MATCH_SERVICE_ATTR, OngoingMatchService.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /match-score");
        String uuid = req.getParameter("uuid");
        MatchScoreDto matchScore = ongoingMatchService.findMatchScore(uuid);
        req.setAttribute("matchScore", matchScore);
        req.getRequestDispatcher(MATCH_SCORE_JSP).forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("POST /match-score");
        String uuid = req.getParameter("uuid");
        String winner = req.getParameter("winner");
        boolean isFinished = ongoingMatchService.processMatchScore(uuid, winner);

        if (isFinished) {
            resp.sendRedirect(req.getContextPath() + ServletPaths.MATCHES);
        } else {
            resp.sendRedirect(req.getContextPath() + ServletPaths.MATCH_SCORE_WITH_UUID + uuid);
        }
    }
}
