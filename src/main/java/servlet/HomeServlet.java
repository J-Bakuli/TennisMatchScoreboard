package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@WebServlet({"/home"})
@Slf4j
public class HomeServlet extends HttpServlet {
    private static final String HOME_JSP = "/WEB-INF/views/home.jsp";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /home");
        req.getRequestDispatcher(HOME_JSP).forward(req, resp);
    }
}
