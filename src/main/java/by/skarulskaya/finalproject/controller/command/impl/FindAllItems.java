package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.CustomEntity;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.service.impl.CategoryService;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;

import static by.skarulskaya.finalproject.controller.PagesPaths.CATALOG_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.CATEGORY_LIST;
import static by.skarulskaya.finalproject.controller.Parameters.ITEM_LIST;

public class FindAllItems implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final ItemService itemService = ItemService.getInstance();
    private static final CategoryService categoryService = CategoryService.getInstance();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        try {
            List<Item> itemList = itemService.findAllItems();
            request.setAttribute(ITEM_LIST, itemList);
            router.setCurrentPage(CATALOG_PAGE);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return router;
    }
}
