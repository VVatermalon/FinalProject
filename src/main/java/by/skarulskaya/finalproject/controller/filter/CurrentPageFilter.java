package by.skarulskaya.finalproject.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static by.skarulskaya.finalproject.controller.Parameters.CURRENT_PAGE;

public class CurrentPageFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private static final String CONTROLLER_PATTERN = "/controller?";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        if (!request.getMethod().equals("GET") ||
                (request.getQueryString() != null && request.getQueryString().contains("command=change_language"))) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String requestURI = request.getRequestURI();
        String query = request.getQueryString();
        if (query != null) {
            requestURI = request.getContextPath() + CONTROLLER_PATTERN + query;
        }
        session.setAttribute(CURRENT_PAGE, requestURI);
        logger.debug("Current page " + requestURI);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
