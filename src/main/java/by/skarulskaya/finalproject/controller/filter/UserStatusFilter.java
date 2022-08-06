package by.skarulskaya.finalproject.controller.filter;

import by.skarulskaya.finalproject.controller.command.CommandType;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.service.UserService;
import by.skarulskaya.finalproject.model.service.impl.UserServiceImpl;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static by.skarulskaya.finalproject.controller.PagesPaths.*;
import static by.skarulskaya.finalproject.controller.Parameters.*;

@WebFilter(filterName = "userStatusFilter", urlPatterns = "/*")
public class UserStatusFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private static final String EMPTY_STRING = "";
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER);
        Customer customer = (Customer) session.getAttribute(CUSTOMER);
        if (customer == null && (user == null || user.getRole() == User.Role.GUEST)) {
            filterChain.doFilter(request, response);
            return;
        }
        String requestURI = request.getServletPath();
        String command = request.getParameter(COMMAND);
        if (requestURI.contains(USER_BLOCKED_PAGE) ||
                (command != null && command.equalsIgnoreCase(CommandType.SIGN_OUT.name()))) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        int userId = customer != null ? customer.getId() : user.getId();
        try {
            Optional<User> userOptional = userService.findUserById(userId);
            if (userOptional.isEmpty()) {
                logger.warn("User deleted");
                deleteCookie(USER_EMAIL, response);
                deleteCookie(USER_PASSWORD, response);
                session.invalidate();
                response.sendRedirect(request.getContextPath() + START_PAGE);
                return;
            }
            if (userOptional.get().getStatus() == User.Status.BLOCKED) {
                logger.warn("User blocked");
                response.sendRedirect(request.getContextPath() + USER_BLOCKED_PAGE);
                return;
            }
        } catch (ServiceException e) {
            ((HttpServletResponse) servletResponse).sendError(500);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, EMPTY_STRING);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
