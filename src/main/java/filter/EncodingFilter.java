package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "filter1")
public class EncodingFilter extends HttpFilter {
    private static final String CHARSET_UTF8 = "UTF-8";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        res.setCharacterEncoding(CHARSET_UTF8);
        req.setCharacterEncoding(CHARSET_UTF8);
        super.doFilter(req, res, chain);
    }
}
