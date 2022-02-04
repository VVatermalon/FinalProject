package by.skarulskaya.finalproject.controller.filter;

import jakarta.servlet.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class EncodingFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private static final String ENCODING_PARAMETER = "encoding";
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Init EncodingFilter");
        encoding = filterConfig.getInitParameter(ENCODING_PARAMETER);
        logger.debug("Filter encoding: " + encoding);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String requestEncoding = servletRequest.getCharacterEncoding();
        if (encoding != null && !encoding.equalsIgnoreCase(requestEncoding)) {
            servletRequest.setCharacterEncoding(encoding);
            servletResponse.setCharacterEncoding(encoding);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("Destroy EncodingFilter");
        encoding = null;
    }
}
