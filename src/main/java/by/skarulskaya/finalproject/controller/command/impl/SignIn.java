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

public class SignIn implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService = UserService.getInstance();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        Router router = new Router();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        logger.debug(email, password);
        Optional<User> userOptional;
        try {
            userOptional = userService.signIn(email, password);
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            session.setAttribute("user", user);
            switch(user.getRole()) {
                case ADMIN -> {
                    router.setCurrentPage("admin page");
                }
                case CUSTOMER -> {
                    if(user.getStatus() == User.Status.BLOCKED) {
                        session.setAttribute("message", "You're blocked");
                        router.setCurrentPage("sign in");
                    }
                    else {
                        logger.info("Client page");
                        router.setCurrentPage("client page");
                    }
                }
            }
        }
        else {
            session.setAttribute("error", "error incorrect log or pass");
            router.setCurrentPage("sign in");
        }
        return router;
    }
}
