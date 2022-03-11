package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Order;
import by.skarulskaya.finalproject.model.service.impl.CustomerService;
import by.skarulskaya.finalproject.model.service.impl.OrderService;
import by.skarulskaya.finalproject.model.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;
import static by.skarulskaya.finalproject.controller.PagesPaths.*;

public class Registration implements Command {
    private final CustomerService customerService = CustomerService.getInstance();
    private final OrderService orderService = OrderService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Map<String, String> mapData = new HashMap<>();
        mapData.put(USER_EMAIL, request.getParameter(USER_EMAIL));
        mapData.put(USER_PASSWORD, request.getParameter(USER_PASSWORD));
        mapData.put(USER_NAME, request.getParameter(USER_NAME));
        mapData.put(USER_SURNAME, request.getParameter(USER_SURNAME));
        mapData.put(USER_PHONE_NUMBER, request.getParameter(USER_PHONE_NUMBER));
        Router router = new Router();
        try {
            int createdCustomerId = customerService.registerCustomer(mapData);
            if (createdCustomerId >= 0) {
                Order newCart = new Order();
                orderService.createCart(createdCustomerId, newCart);
                router.setCurrentType(Router.Type.REDIRECT);
                router.setCurrentPage(request.getContextPath() + SIGN_IN_PAGE);
                return router;
            }
            for (String key : mapData.keySet()) {
                String message = mapData.get(key);
                switch (message) {
                    case INVALID_EMAIL -> {
                        request.setAttribute(INVALID_EMAIL, INVALID_EMAIL_MESSAGE);
                        mapData.put(key, null);
                    }
                    case WRONG_EMAIL -> {
                        request.setAttribute(INVALID_EMAIL, WRONG_EMAIL_MESSAGE);
                        mapData.put(key, null);
                    }
                    case INVALID_PASSWORD -> {
                        request.setAttribute(INVALID_PASSWORD, INVALID_PASSWORD_MESSAGE);
                        mapData.put(key, null);
                    }
                    case INVALID_NAME -> {
                        request.setAttribute(INVALID_NAME, INVALID_NAME_MESSAGE);
                        mapData.put(key, null);
                    }
                    case INVALID_SURNAME -> {
                        request.setAttribute(INVALID_SURNAME, INVALID_SURNAME_MESSAGE);
                        mapData.put(key, null);
                    }
                    case INVALID_PHONE_NUMBER -> {
                        request.setAttribute(INVALID_PHONE_NUMBER, INVALID_PHONE_NUMBER_MESSAGE);
                        mapData.put(key, null);
                    }
                    case NOT_UNIQUE_EMAIL -> {
                        request.setAttribute(INVALID_EMAIL, NOT_UNIQUE_EMAIL_MESSAGE);
                        mapData.put(key, null);
                    }
                    case NOT_UNIQUE_PHONE -> {
                        request.setAttribute(INVALID_PHONE_NUMBER, NOT_UNIQUE_PHONE_MESSAGE);
                        mapData.put(key, null);
                    }
                }
            }
            request.setAttribute(USER_DATA_MAP, mapData);
            router.setCurrentPage(REGISTRATION_PAGE);
            return router;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
