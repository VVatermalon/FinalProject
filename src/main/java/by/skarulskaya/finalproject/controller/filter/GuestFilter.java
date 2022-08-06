package by.skarulskaya.finalproject.controller.filter;

import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.*;
import by.skarulskaya.finalproject.model.service.CustomerService;
import by.skarulskaya.finalproject.model.service.OrderComponentService;
import by.skarulskaya.finalproject.model.service.OrderService;
import by.skarulskaya.finalproject.model.service.UserService;
import by.skarulskaya.finalproject.model.service.impl.CustomerServiceImpl;
import by.skarulskaya.finalproject.model.service.impl.OrderComponentServiceImpl;
import by.skarulskaya.finalproject.model.service.impl.OrderServiceImpl;
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
import java.util.*;

import static by.skarulskaya.finalproject.controller.Parameters.*;

@WebFilter(filterName = "guestFilter", urlPatterns = "/*")
public class GuestFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private static final String EMPTY_STRING = "";
    private final UserService userService = UserServiceImpl.getInstance();
    private final CustomerService customerService = CustomerServiceImpl.getInstance();
    private final OrderService orderService = OrderServiceImpl.getInstance();
    private final OrderComponentService orderComponentService = OrderComponentServiceImpl.getInstance();

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
            Optional<Cookie> userEmailOptional = Arrays.stream(cookies).
                    filter(cookie -> cookie.getName().equals(USER_EMAIL)).findFirst();
            Optional<Cookie> userPasswordOptional = Arrays.stream(cookies).
                    filter(cookie -> cookie.getName().equals(USER_PASSWORD)).findFirst();
            if (userPasswordOptional.isPresent() && userEmailOptional.isPresent()) {
                String userEmail = userEmailOptional.get().getValue();
                String userPassword = userPasswordOptional.get().getValue();
                logger.info("Found user in cookies, email = " + userEmail);
                if (addUserToSession(userEmail, userPassword, session)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            deleteCookie(USER_EMAIL, response);
            deleteCookie(USER_PASSWORD, response);
        }
        logger.info("No user in cookies and session, add guest user");
        user = new User();
        user.setRole(User.Role.GUEST);
        session.setAttribute(USER, user);
        filterChain.doFilter(request, response);
    }

    private boolean addUserToSession(String email, String password, HttpSession session) {
        try {
            Optional<User> userOptional = userService.findUserByEmailAndPassword(email, password);
            if (userOptional.isEmpty()) {
                return false;
            }
            User user = userOptional.get();
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

    private void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, EMPTY_STRING);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
