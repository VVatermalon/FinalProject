package by.skarulskaya.finalproject.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static by.skarulskaya.finalproject.controller.Parameters.LANGUAGE;

@WebFilter(filterName = "languageFilter", urlPatterns = "/*")
public class LanguageFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();
        if(session.getAttribute(LANGUAGE) != null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (cookies != null) {
            Optional<Cookie> optionalCookie = Arrays.stream(cookies).
                    filter(cookie -> cookie.getName().equals(LANGUAGE)).findFirst();
            if (optionalCookie.isPresent()) {
                Cookie cookie = optionalCookie.get();
                String language = cookie.getValue();
                session.setAttribute(LANGUAGE, language);
                logger.info("Language Filter added language from cookie: " + language);
            }
        }
        filterChain.doFilter(request, response);
    }
}
