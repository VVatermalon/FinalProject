package by.skarulskaya.finalproject.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(filterName = "xssProtectionFilter", urlPatterns = "/*")
public class XssProtectionFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private static final String CONTROLLER_PATTERN = "/controller?";
    private static final String REGEX_SCRIPT = "%3C|%3E|%27";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse =(HttpServletResponse) response;
        String requestURI = servletRequest.getRequestURI();
        String query = servletRequest.getQueryString();
        if(query != null){
            requestURI = CONTROLLER_PATTERN + query;
        }
        Pattern pattern = Pattern.compile(REGEX_SCRIPT);
        Matcher matcher = pattern.matcher(requestURI);
        if(matcher.find()){
            logger.warn("found script: " + requestURI);
            servletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(request,response);
    }
}