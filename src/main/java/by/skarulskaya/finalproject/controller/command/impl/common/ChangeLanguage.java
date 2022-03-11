package by.skarulskaya.finalproject.controller.command.impl.common;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.skarulskaya.finalproject.controller.Parameters.*;

public class ChangeLanguage implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String ENGLISH = "en_US";
    private static final String RUSSIAN = "ru_RU";

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Router router = new Router();
        String currentPage = (String) session.getAttribute(PAGE);
        String language = request.getParameter(LANGUAGE);
        logger.debug("Language parameter is " + language);
        if (language == null || (!language.equals(ENGLISH) && !language.equals(RUSSIAN))) {
            router.setCurrentPage(currentPage);
            return router;
        }
        logger.debug("Current page is " + currentPage);
        session.setAttribute(LANGUAGE, language);
        Cookie cookie = new Cookie(LANGUAGE, language);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(cookie);
        router.setCurrentType(Router.Type.REDIRECT);
        router.setCurrentPage(currentPage);
        return router;
    }
}
