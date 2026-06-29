package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public class BaseServlet extends HttpServlet {

    // Класс наследуется от HttpServlet, но при этом не является сервлетом (не занимается обработкой HTTP-запросов),
        // а предоставляет вспомогательную утилиту для получения атрибутов.
        // Можно убрать наследование от HttpServlet и передавать ServletContext в качестве параметра в метод.


    // Название targetClass было бы более понятным для параметра Class<T> tClass
    protected <T> T getRequiredAttribute(String attributeName, Class<T> tClass) throws ServletException {
        Object value = getServletContext().getAttribute(attributeName);
        if (value == null) {
            String msg = String.format("%s is not initialized in ServletContext", attributeName);
            throw new ServletException(msg);
        }
        if (!tClass.isInstance(value)) {
            String msg = String.format("Attribute %s has unexpected type. Expected: %s, actual: %s", attributeName, tClass.getName(), value.getClass().getName());
            throw new ServletException(msg);
        }
        return tClass.cast(value);
    }
}
