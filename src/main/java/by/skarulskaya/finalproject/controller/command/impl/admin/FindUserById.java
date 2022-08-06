package by.skarulskaya.finalproject.controller.command.impl.admin;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.service.UserService;
import by.skarulskaya.finalproject.model.service.impl.UserServiceImpl;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

import static by.skarulskaya.finalproject.controller.PagesPaths.USERS_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.INVALID_USER_ID_MESSAGE;

public class FindUserById  implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final int FIRST_PAGINATION_PAGE = 1;
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String userIdParameter = request.getParameter(USER_ID);
        if(!BaseValidatorImpl.getInstance().validateId(userIdParameter)) {
            request.setAttribute(INVALID_USER_ID, INVALID_USER_ID_MESSAGE);
            router.setCurrentPage(USERS_PAGE);
            return router;
        }
        try {
            int userId = Integer.parseInt(userIdParameter);
            Optional<User> userOptional = userService.findUserById(userId);
            userOptional.ifPresent(user -> request.setAttribute(USER_LIST, List.of(user)));
        } catch (ServiceException | NumberFormatException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        request.setAttribute(PAGE, FIRST_PAGINATION_PAGE);
        router.setCurrentPage(USERS_PAGE);
        return router;
    }
}

