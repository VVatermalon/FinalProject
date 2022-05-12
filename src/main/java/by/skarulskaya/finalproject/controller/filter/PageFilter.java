package by.skarulskaya.finalproject.controller.filter;

import by.skarulskaya.finalproject.controller.filter.access.PageAccess;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Set;

import static by.skarulskaya.finalproject.controller.Parameters.CUSTOMER;
import static by.skarulskaya.finalproject.controller.Parameters.USER;

@WebFilter(filterName = "pageFilter", urlPatterns = "*.jsp")
public class PageFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String requestURI = request.getServletPath();
        logger.debug("Page URI: " + requestURI);
        User.Role role = User.Role.GUEST;
        User user = (User) session.getAttribute(USER);
        Customer customer = (Customer) session.getAttribute(CUSTOMER);
        if(user != null){
            role = user.getRole();
        }
        if(customer != null) {
            role  = customer.getRole();
        }
        boolean isIncorrect;
        Set<String> pages;
        switch (role){
            case ADMIN -> {
                pages = PageAccess.ADMIN.getPages();
                isIncorrect = pages.stream().anyMatch(requestURI::contains);
            }
            case CUSTOMER -> {
                pages = PageAccess.CUSTOMER.getPages();
                isIncorrect = pages.stream().anyMatch(requestURI::contains);
            }
            default -> {
                pages = PageAccess.GUEST.getPages();
                isIncorrect = pages.stream().anyMatch(requestURI::contains);
            }
        }
        if(isIncorrect) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        filterChain.doFilter(request,response);
    }
}
