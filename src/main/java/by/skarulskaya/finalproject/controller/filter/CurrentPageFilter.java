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

@WebFilter(filterName = "currentPageFilter", urlPatterns = "/*", initParams = @WebInitParam(name = "excludedURLs", value = "/js/;/images/;/css/"))
public class CurrentPageFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private static final String EXCLUDED_URLS_PARAMETER = "excludedURLs";
    private static final String SPLITTER = ";";
    private static final String CONTROLLER_PATTERN = "/controller?";
    private List<String> excludedURLs;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String[] excluded = filterConfig.getInitParameter(EXCLUDED_URLS_PARAMETER).split(SPLITTER);
        excludedURLs = new ArrayList<>();
        Collections.addAll(excludedURLs, excluded);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();

        String requestURI = request.getRequestURI();
        if(excludedURLs.stream().anyMatch(requestURI::contains)) {
            filterChain.doFilter(request, servletResponse);
            return;
        }

        if (!request.getMethod().equals("GET") ||
                (request.getQueryString() != null && request.getQueryString().contains("command=change_language"))) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String query = request.getQueryString();
        if (query != null) {
            requestURI = request.getContextPath() + CONTROLLER_PATTERN + query;
        }
        session.setAttribute(PAGE, requestURI);
        logger.debug("Current page " + requestURI);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
