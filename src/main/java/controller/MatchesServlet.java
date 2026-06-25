package controller;

import db.AppLifecycleListener;
import dto.FinishedMatchesPageDto;
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

    // Все повторяющиеся или важные строковые литералы лучше выносить в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    private FinishedMatchesService finishedMatchesService;

    @Override
    public void init() throws ServletException {

        // Для получения объектов из контекста можно использовать "естественные константы" — ClassName.class.getSimpleName() или ClassName.class.getName()
        finishedMatchesService = getRequiredAttribute(
                AppLifecycleListener.FINISHED_MATCHES_SERVICE_ATTR, FinishedMatchesService.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /matches");
        FinishedMatchesPageDto matchesPage = finishedMatchesService.getFinishedMatchesPage(
                req.getParameter("page"),
                req.getParameter("filter_by_player_name")
        );
        req.setAttribute("matchesPage", matchesPage);
        req.getRequestDispatcher("/WEB-INF/views/matches.jsp").forward(req, resp);
    }
}
