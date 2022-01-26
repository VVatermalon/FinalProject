package by.skarulskaya.finalproject.controller;

import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.controller.command.CommandType;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ConnectionPoolException;
import by.skarulskaya.finalproject.model.pool.CustomConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(urlPatterns = {"/controller"})
public class CommonController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug(req.getMethod());
        processRequest(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug(req.getMethod());
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter("command");
        logger.debug(commandName);
        try {
            Command command = CommandType.provideCommand(commandName);
            logger.info(command);
            Router router;
            router = command.execute(req);
            String page = router.getCurrentPage();
            if (router.getCurrentType() == Router.Type.FORWARD) {
                logger.debug("forward");
                req.getRequestDispatcher(page).forward(req, resp);
            } else {
                logger.debug("redirect");
                resp.sendRedirect(page);
            }
        } catch (CommandException e) {
            logger.error(e);
            Router router = new Router();
            router.setCurrentPage("error 500");
            resp.sendRedirect(router.getCurrentPage());
        }
    }

    @Override
    public void destroy() {
        CustomConnectionPool.INSTANCE.destroyPool();
    }
}
