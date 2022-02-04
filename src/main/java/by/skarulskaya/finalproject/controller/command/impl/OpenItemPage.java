package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;
import static by.skarulskaya.finalproject.controller.PagesPaths.*;

public class OpenItemPage implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final ItemService itemService = ItemService.getInstance();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        int id = Integer.parseInt(request.getParameter(ID));
        logger.info("Find item id = "+id);
        Optional<Item> itemOptional;
        try {
            itemOptional = itemService.findItemById(id);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        if (itemOptional.isPresent()) {
            request.setAttribute(ITEM, itemOptional.get());
            router.setCurrentPage(ITEM_PAGE);
        } else {
            throw new CommandException("Cannot find item by id: " + id);
        }
        return router;
    }
}
