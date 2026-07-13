package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@WebServlet({HomeServlet.HOME_PATH})
@Slf4j
public class HomeServlet extends HttpServlet {

    private static final String HOME_PATH = "/home";
    private static final String HOME_JSP = "/WEB-INF/views/home.jsp";
    private static final String GET_HOME_LOG = "GET " + HOME_PATH;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info(GET_HOME_LOG);
        req.getRequestDispatcher(HOME_JSP).forward(req, resp);
    }
}
