package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;
import static by.skarulskaya.finalproject.controller.PagesPaths.*;

public class SignIn implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService = UserService.getInstance();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
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
            session.setAttribute(USER, user);
            switch(user.getRole()) {
                case ADMIN -> {
                    router.setCurrentPage(ADMIN_PAGE);
                    logger.info("admin");
                }
                case CUSTOMER -> {
                    if(user.getStatus() == User.Status.BLOCKED) {
                        logger.info("blocked");
                        session.setAttribute(USER_STATUS_BLOCKED, USER_BLOCKED_MESSAGE);
                        router.setCurrentPage(SIGN_IN_PAGE);
                    }
                    else {
                        logger.info("Client page");
                        router.setCurrentPage(CLIENT_PAGE);
                    }
                }
            }
        }
        else {
            session.setAttribute(ERROR_INCORRECT_LOGIN_OR_PASSWORD, ERROR_INCORRECT_LOGIN_OR_PASSWORD_MESSAGE);
            router.setCurrentPage(SIGN_IN_PAGE);
        }
        return router;
    }
}
