package controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BaseServlet {
    public static <T> T getRequiredAttribute(
            ServletContext servletContext, String attributeName, Class<T> targetClass) throws ServletException {
        Object value = servletContext.getAttribute(attributeName);
        if (value == null) {
            String msg = String.format("%s is not initialized in ServletContext", attributeName);
            throw new ServletException(msg);
        }
        if (!targetClass.isInstance(value)) {
            String msg = String.format(
                    "Attribute %s has unexpected type. Expected: %s, actual: %s",
                    attributeName, targetClass.getName(), value.getClass().getName());
            throw new ServletException(msg);
        }
        return targetClass.cast(value);
    }
}
