package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.model.entity.*;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.service.impl.CustomerService;
import by.skarulskaya.finalproject.model.service.impl.OrderComponentService;
import by.skarulskaya.finalproject.model.service.impl.OrderService;
import by.skarulskaya.finalproject.model.service.impl.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;
import static by.skarulskaya.finalproject.controller.PagesPaths.*;

public class SignIn implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService = UserService.getInstance();
    private final CustomerService customerService = CustomerService.getInstance();
    private final OrderService orderService = OrderService.getInstance();
    private final OrderComponentService orderComponentService = OrderComponentService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        HttpSession session = request.getSession();
        Router router = new Router();
        String email = request.getParameter(USER_EMAIL);
        String password = request.getParameter(USER_PASSWORD);
        Optional<User> userOptional;
        try {
            userOptional = userService.signIn(email, password);
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            processRole(user, session, response, router);
        }
        else {
            request.setAttribute(ERROR_INCORRECT_LOGIN_OR_PASSWORD, ERROR_INCORRECT_LOGIN_OR_PASSWORD_MESSAGE);
            router.setCurrentPage(SIGN_IN_PAGE);
        }
        return router;
    }

    private void processRole(User user, HttpSession session, HttpServletResponse response, Router router) throws CommandException {
        switch(user.getRole()) {
            case ADMIN -> {
                session.setAttribute(USER, user);
                Cookie cookie = new Cookie(USER, String.valueOf(user.getId()));
                cookie.setMaxAge(COOKIE_MAX_AGE);
                response.addCookie(cookie);

                router.setCurrentPage(ADMIN_PAGE);
            }
            case CUSTOMER -> {
                if (user.getStatus() == User.Status.BLOCKED) {
                    logger.info("blocked");
                    session.setAttribute(USER_STATUS_BLOCKED, USER_BLOCKED_MESSAGE);
                    router.setCurrentPage(SIGN_IN_PAGE); //todo blocking page
                } else {
                    try {
                        Optional<Customer> customerOptional = customerService.findCustomerById(user.getId());
                        if (customerOptional.isPresent()) {
                            session.setAttribute(CUSTOMER, customerOptional.get());
                            Cookie cookie = new Cookie(USER, String.valueOf(user.getId()));
                            cookie.setMaxAge(COOKIE_MAX_AGE);
                            response.addCookie(cookie);
                            session.removeAttribute(USER);

                            int cartOrderId = orderService.findCartOrderId(user.getId());
                            session.setAttribute(CART_ORDER_ID, cartOrderId);
                            int itemsInCartCount = orderComponentService.countItemsInCart(cartOrderId);
                            session.setAttribute(ITEMS_IN_CART_COUNT, itemsInCartCount);

                            router.setCurrentPage(START_PAGE);
                        } else {
                            throw new CommandException("Error with data: cannot find linked to user customer");
                        }
                    } catch (ServiceException e) {
                        throw new CommandException(e);
                    }
                }
            }
        }
    }
}
