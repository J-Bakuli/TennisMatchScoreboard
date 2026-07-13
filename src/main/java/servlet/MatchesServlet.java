package servlet;

import dto.FinishedMatchesPageDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import service.FinishedMatchesService;
import util.ServletContextUtils;

import java.io.IOException;

@WebServlet({"/matches"})
@Slf4j
public class MatchesServlet extends HttpServlet {
    private static final String MATCHES_JSP = "/WEB-INF/views/matches.jsp";
    private FinishedMatchesService finishedMatchesService;

    @Override
    public void init() throws ServletException {
        finishedMatchesService = ServletContextUtils.getRequiredAttribute(
                getServletContext(), FinishedMatchesService.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /matches");
        FinishedMatchesPageDto matchesPage = finishedMatchesService.getFinishedMatchesPage(
                req.getParameter("page"),
                req.getParameter("filter_by_player_name")
        );
        req.setAttribute("matchesPage", matchesPage);
        req.getRequestDispatcher(MATCHES_JSP).forward(req, resp);
    }
}
