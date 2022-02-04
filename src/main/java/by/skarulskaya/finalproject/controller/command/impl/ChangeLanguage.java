package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.skarulskaya.finalproject.controller.Parameters.CURRENT_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.LANGUAGE;

public class ChangeLanguage implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String ENGLISH = "en_US";
    private static final String RUSSIAN = "ru_RU";

    @Override
    public Router execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Router router = new Router();
        String currentPage = (String) session.getAttribute(CURRENT_PAGE);
        String language = request.getParameter(LANGUAGE);
        logger.debug("Language parameter is " + language);
        if(language==null || (!language.equals(ENGLISH) && !language.equals(RUSSIAN))){
            router.setCurrentPage(currentPage);
            return router;
        }
        logger.debug("Current page is " + currentPage);
        session.setAttribute(LANGUAGE,language);
        router.setCurrentType(Router.Type.REDIRECT);
        router.setCurrentPage(currentPage);
        return router;
    }
}
