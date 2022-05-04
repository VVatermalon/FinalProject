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

import static by.skarulskaya.finalproject.controller.PagesPaths.ERROR_500;
import static by.skarulskaya.finalproject.controller.Parameters.COMMAND;

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
        String commandName = req.getParameter(COMMAND);
        logger.debug(commandName);
        try {
            Command command = CommandType.provideCommand(commandName);
            Router router;
            router = command.execute(req, resp);
            String page = router.getCurrentPage();
            if (router.getCurrentType() == Router.Type.FORWARD) {
                req.getRequestDispatcher(page).forward(req, resp);
            } else {
                resp.sendRedirect(page);
            }
        } catch (CommandException e) { //todo добавить описание ошибки, плюс еще какие ошибки могут быть
            logger.error(e);
            resp.sendRedirect(ERROR_500);
        }
    }

    @Override
    public void destroy() {
        CustomConnectionPool.INSTANCE.destroyPool();
    }
}
