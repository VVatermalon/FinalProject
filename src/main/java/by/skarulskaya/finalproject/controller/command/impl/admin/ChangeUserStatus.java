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

public class ChangeUserStatus  implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        try {
            String userIdParameter = request.getParameter(USER_ID);
            String userStatusParameter = request.getParameter(USER_STATUS);
            if(userIdParameter == null || userStatusParameter == null) {
                router.setCurrentPage(ERROR_404);
                return router;
            }
            int userId = Integer.parseInt(userIdParameter);
            User.Status userStatus = User.Status.valueOf(userStatusParameter);
            if(!userService.changeUserStatus(userId, userStatus)) {
                logger.error("Can't change user status");
                throw new CommandException("Can't change user status, user id = " + userId);
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
