package by.skarulskaya.finalproject.controller.command.impl.admin;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.service.impl.CustomerService;
import by.skarulskaya.finalproject.model.service.impl.OrderService;
import by.skarulskaya.finalproject.model.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import static by.skarulskaya.finalproject.controller.PagesPaths.REGISTRATION_PAGE;
import static by.skarulskaya.finalproject.controller.PagesPaths.SIGN_IN_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.NOT_UNIQUE_PHONE_MESSAGE;

public class RegisterAdmin implements Command {
    private final UserService userService = UserService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Map<String, String> mapData = new HashMap<>();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        mapData.put(USER_EMAIL, request.getParameter(USER_EMAIL));
        mapData.put(USER_PASSWORD, request.getParameter(USER_PASSWORD));
        mapData.put(USER_NAME, request.getParameter(USER_NAME));
        mapData.put(USER_SURNAME, request.getParameter(USER_SURNAME));
        Router router = new Router();
        try {
            if (userService.registerAdmin(mapData)) {
                router.setCurrentType(Router.Type.REDIRECT);
                router.setCurrentPage(request.getContextPath() + currentPage);
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
                    case NOT_UNIQUE_EMAIL -> {
                        request.setAttribute(INVALID_EMAIL, NOT_UNIQUE_EMAIL_MESSAGE);
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

