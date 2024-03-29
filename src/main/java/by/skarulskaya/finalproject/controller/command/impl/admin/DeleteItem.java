package by.skarulskaya.finalproject.controller.command.impl.admin;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.service.ItemService;
import by.skarulskaya.finalproject.model.service.impl.ItemServiceImpl;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.skarulskaya.finalproject.controller.PagesPaths.*;
import static by.skarulskaya.finalproject.controller.Parameters.*;

public class DeleteItem   implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final ItemService itemService = ItemServiceImpl.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        int id;
        try {
            String itemIdParameter = request.getParameter(ITEM_ID);
            if (!BaseValidatorImpl.getInstance().validateId(itemIdParameter)) {
                router.setCurrentPage(ERROR_404);
                return router;
            }
            id = Integer.parseInt(itemIdParameter);
            if (!itemService.delete(id)) {
                logger.error("Can't delete item");
                throw new CommandException("Can't delete item, item id = " + id);
            }
            router.setCurrentType(Router.Type.REDIRECT);
            router.setCurrentPage(request.getContextPath() + currentPage);
            return router;
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
    }
}