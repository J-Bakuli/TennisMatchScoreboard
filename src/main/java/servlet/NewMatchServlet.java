package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import service.NewMatchService;
import util.ServletContextUtils;
import validation.PlayerValidation;

import java.io.IOException;
import java.util.UUID;

@WebServlet({"/new-match"})
@Slf4j
public class NewMatchServlet extends HttpServlet {
    private static final String NEW_MATCH_JSP = "/WEB-INF/views/new-match.jsp";
    private NewMatchService newMatchService;

    @Override
    public void init() throws ServletException {
        newMatchService = ServletContextUtils.getRequiredAttribute(
                getServletContext(), NewMatchService.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /new-match");
        req.getRequestDispatcher(NEW_MATCH_JSP).forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String player1Name = req.getParameter("player1Name");
        String player2Name = req.getParameter("player2Name");

        log.info("POST /new-match - received: player1Name='{}', player2Name='{}'",
                player1Name != null ? player1Name : "null",
                player2Name != null ? player2Name : "null");

        PlayerValidation.validatePlayerNames(player1Name, player2Name);

        UUID matchId = newMatchService.startNewMatch(player1Name, player2Name);
        resp.sendRedirect(req.getContextPath() + ServletPaths.MATCH_SCORE_WITH_UUID + matchId);
    }
}
