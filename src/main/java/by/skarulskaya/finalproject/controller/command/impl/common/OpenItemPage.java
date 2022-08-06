package by.skarulskaya.finalproject.controller.command.impl.common;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.service.ItemService;
import by.skarulskaya.finalproject.model.service.impl.ItemServiceImpl;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.PagesPaths.*;

public class OpenItemPage implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final ItemService itemService = ItemServiceImpl.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        Optional<Item> itemOptional;
        int id;
        try {
            String itemIdParameter = request.getParameter(ITEM_ID);
            if(!BaseValidatorImpl.getInstance().validateId(itemIdParameter)) {
                router.setCurrentPage(ERROR_404);
                return router;
            }
            id = Integer.parseInt(itemIdParameter);
            itemOptional = itemService.findItemById(id);
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        if (itemOptional.isPresent()) {
            request.setAttribute(ITEM, itemOptional.get());
            router.setCurrentPage(ITEM_PAGE);
        } else {
            logger.error("Can't find item by id, id = {}", id);
            router.setCurrentPage(ERROR_500);
        }
        return router;
    }
}
