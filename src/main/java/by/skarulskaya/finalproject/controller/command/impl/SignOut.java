package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.skarulskaya.finalproject.controller.PagesPaths.START_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.COOKIE_MAX_AGE;
import static by.skarulskaya.finalproject.controller.Parameters.USER;

public class SignOut implements Command {
    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        HttpSession session = request.getSession();
        Router router = new Router();
        Cookie newUserCookie = new Cookie(USER, "");
        newUserCookie.setMaxAge(0);
        response.addCookie(newUserCookie);
        session.invalidate();
        router.setCurrentType(Router.Type.REDIRECT);
        router.setCurrentPage(request.getContextPath() + START_PAGE);
        return router;
    }
}
