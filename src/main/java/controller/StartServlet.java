package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@WebServlet({"/home"})
@Slf4j
public class StartServlet extends BaseServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /home");
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
    }
}
