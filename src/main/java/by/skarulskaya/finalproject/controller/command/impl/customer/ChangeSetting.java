package by.skarulskaya.finalproject.controller.command.impl.customer;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Address;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.service.impl.CustomerService;
import by.skarulskaya.finalproject.model.service.impl.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
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
        User user = (User) session.getAttribute(USER);
        if(user == null) {
            user = customer;
        }
        String settingName = request.getParameter(SETTING);
        if(settingName == null) {
            router.setCurrentPage(ERROR_404);
            return router;
        }
        try {
            switch (settingName) {
                case SETTING_FIRST_NAME -> {
                    return changeName(request, router, user);
                }
                case SETTING_LAST_NAME -> {
                    return changeSurname(request, router, user);
                }
                case SETTING_PHONE_NUMBER -> {
                    return changePhoneNumber(request, router, customer);
                }
                case SETTING_PASSWORD -> {
                    return changePassword(request, response, router, user);
                }
                default -> throw new CommandException("Wrong setting parameter " + settingName);
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    private Router changeName(HttpServletRequest request, Router router, User user) throws ServiceException {
        String userName = request.getParameter(USER_NAME);
        if (userService.updateName(user.getId(), userName)) {
            user.setName(userName);
            router.setCurrentType(Router.Type.REDIRECT);
            router.setCurrentPage(request.getContextPath() + SETTINGS_PAGE);
            return router;
        }
        request.setAttribute(INVALID_NAME, INVALID_NAME_MESSAGE);
        router.setCurrentPage(SETTINGS_PAGE);
        return router;
    }

    private Router changeSurname(HttpServletRequest request, Router router, User user) throws ServiceException {
        String userSurname = request.getParameter(USER_SURNAME);
        if (userService.updateSurname(user.getId(), userSurname)) {
            user.setSurname(userSurname);
            router.setCurrentType(Router.Type.REDIRECT);
            router.setCurrentPage(request.getContextPath() + SETTINGS_PAGE);
            return router;
        }
        request.setAttribute(INVALID_SURNAME, INVALID_SURNAME_MESSAGE);
        router.setCurrentPage(SETTINGS_PAGE);
        return router;
    }

    private Router changePhoneNumber(HttpServletRequest request, Router router, Customer customer) throws ServiceException {
        if(customer == null) {
            router.setCurrentPage(ERROR_403);
            return router;
        }
        HashMap<String, String> mapData = new HashMap<>();
        String userPhone = request.getParameter(USER_PHONE_NUMBER);
        mapData.put(USER_PHONE_NUMBER, userPhone);
        mapData.put(USER_ID, String.valueOf(customer.getId()));
        if (customerService.updatePhoneNumber(mapData)) {
            customer.setPhoneNumber(userPhone);
            router.setCurrentType(Router.Type.REDIRECT);
            router.setCurrentPage(request.getContextPath() + SETTINGS_PAGE);
            return router;
        }
        for (String key : mapData.keySet()) {
            String message = mapData.get(key);
            switch (message) {
                case INVALID_PHONE_NUMBER -> request.setAttribute(INVALID_PHONE_NUMBER, INVALID_PHONE_NUMBER_MESSAGE);
                case NOT_UNIQUE_PHONE -> request.setAttribute(INVALID_PHONE_NUMBER, NOT_UNIQUE_PHONE_MESSAGE);
            }
        }
        router.setCurrentPage(SETTINGS_PAGE);
        return router;
    }

    private Router changePassword(HttpServletRequest request, HttpServletResponse response, Router router, User user) throws ServiceException {
        String oldPassword = request.getParameter(USER_OLD_PASSWORD);
        String newPassword = request.getParameter(USER_NEW_PASSWORD);
        String repeatedPassword = request.getParameter(USER_REPEAT_PASSWORD);
        if (!newPassword.equals(repeatedPassword)) {
            request.setAttribute(INVALID_REPEAT_PASSWORD, INVALID_REPEAT_PASSWORD_MESSAGE);
            router.setCurrentPage(SETTINGS_PAGE);
            return router;
        }
        if (!userService.checkCorrectPassword(user.getId(), oldPassword)) {
            request.setAttribute(INVALID_OLD_PASSWORD, INVALID_OLD_PASSWORD_MESSAGE);
            router.setCurrentPage(SETTINGS_PAGE);
            return router;
        }
        if (userService.updatePassword(user.getId(), newPassword)) {
            user.setPassword(newPassword);
            if(user.getRole() == User.Role.ADMIN && user.getStatus() == User.Status.IN_REGISTRATION_PROCESS) {
                user.setStatus(User.Status.ACTIVE);
            }
            deleteCookie(USER_EMAIL, response);
            deleteCookie(USER_PASSWORD, response);
            request.getSession().invalidate();
            router.setCurrentType(Router.Type.REDIRECT);
            router.setCurrentPage(request.getContextPath() + SIGN_IN_PAGE);
            return router;
        }
        request.setAttribute(INVALID_PASSWORD, INVALID_PASSWORD_MESSAGE);
        router.setCurrentPage(SETTINGS_PAGE);
        return router;
    }

    private void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, EMPTY_STRING);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
