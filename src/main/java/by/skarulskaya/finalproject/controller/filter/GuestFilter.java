package by.skarulskaya.finalproject.controller.filter;

import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.*;
import by.skarulskaya.finalproject.model.service.impl.CustomerService;
import by.skarulskaya.finalproject.model.service.impl.OrderComponentService;
import by.skarulskaya.finalproject.model.service.impl.OrderService;
import by.skarulskaya.finalproject.model.service.impl.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

import static by.skarulskaya.finalproject.controller.PagesPaths.*;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.USER_BLOCKED_MESSAGE;

public class GuestFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService = UserService.getInstance();
    private final CustomerService customerService = CustomerService.getInstance();
    private final OrderService orderService = OrderService.getInstance();
    private final OrderComponentService orderComponentService = OrderComponentService.getInstance();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();
        User user = (User) session.getAttribute(USER);
        Customer customer = (Customer) session.getAttribute(CUSTOMER);
        if (user != null || customer != null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (cookies != null) {
            Optional<Cookie> userIdOptional = Arrays.stream(cookies).
                    filter(cookie -> cookie.getName().equals(USER)).findFirst();
            if (userIdOptional.isPresent()) {
                Cookie userIdCookie = userIdOptional.get();
                int userId = Integer.parseInt(userIdCookie.getValue());
                logger.info("Found user in cookies, id = " + userId);
                if (addUserToSession(userId, session)) {
                    filterChain.doFilter(request, response);
                    return;
                }
                Cookie newUserCookie = new Cookie(USER, "");
                newUserCookie.setMaxAge(0);
                response.addCookie(newUserCookie);
            }
        }
        logger.info("No user in cookies and session, add guest user");
        user = new User();
        user.setRole(User.Role.GUEST);
        session.setAttribute(USER, user);
        filterChain.doFilter(request, response);
    }

    private boolean addUserToSession(int userId, HttpSession session) {
        Optional<User> userOptional;
        try {
            userOptional = userService.findUserById(userId);
            if (userOptional.isEmpty()) {
                return false;
            }
            User user = userOptional.get();
            if (user.getStatus() == User.Status.BLOCKED) {
                logger.info("blocked");
                session.setAttribute(USER_STATUS_BLOCKED, USER_BLOCKED_MESSAGE);
                return false;
                //todo blocking page
            }
            if (user.getRole() == User.Role.ADMIN) {
                session.setAttribute(USER, user);
                logger.info("Added admin to session");
                return true;
            }
            Optional<Customer> customerOptional = customerService.findCustomerById(user.getId());
            if (customerOptional.isEmpty()) {
                logger.error("Error with data: can't find linked to user customer");
                return false;
            }
            session.setAttribute(CUSTOMER, customerOptional.get());
            int cartOrderId = orderService.findCartOrderId(user.getId());
            session.setAttribute(CART_ORDER_ID, cartOrderId);
            int itemsInCartCount = orderComponentService.countItemsInCart(cartOrderId);
            session.setAttribute(ITEMS_IN_CART_COUNT, itemsInCartCount);
            logger.info("Added customer to session");
            return true;
        } catch (ServiceException e) {
            logger.error(e);
            return false;
        }
    }
}
