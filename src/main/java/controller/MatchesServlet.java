package controller;

import dao.H2MatchesDao;
import dao.MatchesDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import service.FinishedMatchesService;

import java.io.IOException;

@WebServlet({"/matches"})
@Slf4j
public class MatchesServlet extends HttpServlet {
    private FinishedMatchesService finishedMatchesService;

    @Override
    public void init() throws ServletException {
        MatchesDao matchesDao = new H2MatchesDao();
        finishedMatchesService = new FinishedMatchesService(matchesDao);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /matches");
        req.getRequestDispatcher("/WEB-INF/views/matches.jsp").forward(req, resp);
    }
}
