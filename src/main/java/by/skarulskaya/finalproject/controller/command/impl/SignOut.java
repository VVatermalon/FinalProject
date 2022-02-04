package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.skarulskaya.finalproject.controller.PagesPaths.START_PAGE;

public class SignOut implements Command {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        Router router = new Router();
        session.invalidate();
        router.setCurrentType(Router.Type.REDIRECT);
        logger.info(request.getContextPath());
        router.setCurrentPage(request.getContextPath()+ START_PAGE);
        return router;
    }
}
