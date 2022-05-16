package by.skarulskaya.finalproject.controller.command.impl.customer;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Address;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.service.impl.CustomerService;
import by.skarulskaya.finalproject.model.service.impl.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static by.skarulskaya.finalproject.controller.PagesPaths.*;
import static by.skarulskaya.finalproject.controller.PagesPaths.ADD_SHIPPING_ADDRESS_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;

public class ChangeSetting implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String EMPTY_STRING = "";
    private final CustomerService customerService = CustomerService.getInstance();
    private final UserService userService = UserService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute(CUSTOMER);
        String settingName = request.getParameter(SETTING);
        try {
            switch (settingName) {
                case SETTING_FIRST_NAME -> {
                    String userName = request.getParameter(USER_NAME);
                    if (userService.updateName(customer.getId(), userName)) {
                        customer.setName(userName);
                        router.setCurrentType(Router.Type.REDIRECT);
                        router.setCurrentPage(request.getContextPath() + SETTINGS_PAGE);
                        return router;
                    }
                    request.setAttribute(INVALID_NAME, INVALID_NAME_MESSAGE);
                    router.setCurrentPage(SETTINGS_PAGE);
                    return router;
                }
                case SETTING_LAST_NAME -> {
                    String userSurname = request.getParameter(USER_SURNAME);
                    if (userService.updateSurname(customer.getId(), userSurname)) {
                        customer.setSurname(userSurname);
                        router.setCurrentType(Router.Type.REDIRECT);
                        router.setCurrentPage(request.getContextPath() + SETTINGS_PAGE);
                        return router;
                    }
                    request.setAttribute(INVALID_SURNAME, INVALID_SURNAME_MESSAGE);
                    router.setCurrentPage(SETTINGS_PAGE);
                    return router;
                }
                case SETTING_PHONE_NUMBER -> {
                    String userPhone = request.getParameter(USER_PHONE_NUMBER);
                    if (customerService.updatePhoneNumber(customer.getId(), userPhone)) {
                        customer.setPhoneNumber(userPhone);
                        router.setCurrentType(Router.Type.REDIRECT);
                        router.setCurrentPage(request.getContextPath() + SETTINGS_PAGE);
                        return router;
                    }
                    request.setAttribute(INVALID_PHONE_NUMBER, INVALID_PHONE_NUMBER_MESSAGE);
                    router.setCurrentPage(SETTINGS_PAGE);
                    return router;
                }
                case SETTING_PASSWORD -> {
                    String oldPassword = request.getParameter(USER_OLD_PASSWORD);
                    String newPassword = request.getParameter(USER_NEW_PASSWORD);
                    String repeatedPassword = request.getParameter(USER_REPEAT_PASSWORD);
                    if (!newPassword.equals(repeatedPassword)) {
                        request.setAttribute(INVALID_REPEAT_PASSWORD, INVALID_REPEAT_PASSWORD_MESSAGE);
                        router.setCurrentPage(SETTINGS_PAGE);
                        return router;
                    }
                    if (!userService.checkCorrectPassword(customer.getId(), oldPassword)) {
                        request.setAttribute(INVALID_OLD_PASSWORD, INVALID_OLD_PASSWORD_MESSAGE);
                        router.setCurrentPage(SETTINGS_PAGE);
                        return router;
                    }
                    if (userService.updatePassword(customer.getId(), newPassword)) {
                        customer.setPassword(newPassword);
                        deleteCookie(USER_EMAIL, response);
                        deleteCookie(USER_PASSWORD, response);
                        session.invalidate();
                        router.setCurrentType(Router.Type.REDIRECT);
                        router.setCurrentPage(request.getContextPath() + SIGN_IN_PAGE);
                        return router;
                    }
                    request.setAttribute(INVALID_PASSWORD, INVALID_PASSWORD_MESSAGE);
                    router.setCurrentPage(SETTINGS_PAGE);
                    return router;
                }
                default -> throw new CommandException("Wrong setting parameter " + settingName);
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    private void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, EMPTY_STRING);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
