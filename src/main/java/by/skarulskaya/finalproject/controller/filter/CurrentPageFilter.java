package by.skarulskaya.finalproject.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

import static by.skarulskaya.finalproject.controller.Parameters.*;

@WebFilter(filterName = "currentPageFilter", urlPatterns = "*.jsp", dispatcherTypes = {DispatcherType.FORWARD, DispatcherType.REQUEST})
public class CurrentPageFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private static final String CONTROLLER = "/controller?";
    private static final String QUESTION = "?";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpSession session = servletRequest.getSession();
        String currentPage;
        String query = servletRequest.getQueryString();
        if (query != null) {
            if (servletRequest.getParameter(COMMAND) != null) {
                currentPage = CONTROLLER + query;
            } else {
                currentPage = servletRequest.getContextPath() + servletRequest.getServletPath() + QUESTION + query;
            }
        }
        else {
            currentPage = servletRequest.getServletPath();
        }
        logger.info(query);
        session.setAttribute(CURRENT_PAGE, currentPage);
        logger.info("final result" + currentPage);
        chain.doFilter(request, response);
    }
}