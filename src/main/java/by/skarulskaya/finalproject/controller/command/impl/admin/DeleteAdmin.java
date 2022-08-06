package by.skarulskaya.finalproject.controller.command.impl.admin;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.service.UserService;
import by.skarulskaya.finalproject.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.skarulskaya.finalproject.controller.PagesPaths.ERROR_404;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.CANNOT_DELETE_CURRENT_ADMIN_MESSAGE;

public class DeleteAdmin implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        User currentAdmin = (User) request.getSession().getAttribute(USER);
        try {
            String userIdParameter = request.getParameter(USER_ID);
            if(userIdParameter == null) {
                router.setCurrentPage(ERROR_404);
                return router;
            }
            int userId = Integer.parseInt(userIdParameter);
            if(userId == currentAdmin.getId()) {
                request.setAttribute(ERROR_DELETE_ADMIN, CANNOT_DELETE_CURRENT_ADMIN_MESSAGE);
                router.setCurrentPage(currentPage);
                return router;
            }
            if(!userService.deleteUser(userId)) {
                logger.error("Can't delete user");
                throw new CommandException("Can't delete user, user id = " + userId);
            }
        } catch (ServiceException | IllegalArgumentException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        router.setCurrentType(Router.Type.REDIRECT);
        router.setCurrentPage(request.getContextPath() + currentPage);
        return router;
    }
}

