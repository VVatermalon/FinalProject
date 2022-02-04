package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static by.skarulskaya.finalproject.controller.PagesPaths.CATALOG_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.CATEGORY_ID;
import static by.skarulskaya.finalproject.controller.Parameters.ITEM_LIST;

public class FindByCategoryItems implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final ItemService itemService = ItemService.getInstance();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        int categoryId = Integer.parseInt(request.getParameter(CATEGORY_ID));
        try {
            List<Item> itemList = (List<Item>)request.getAttribute(ITEM_LIST);
            if(itemList != null) {
                logger.info("you are not stupid");
                itemList = itemList.stream()
                        .filter(i -> i.getCategory().getId() == categoryId)
                        .toList();
            }
            else {
                itemList = itemService.findAllByCategory(categoryId);
            }
            request.setAttribute(ITEM_LIST, itemList);
            router.setCurrentPage(CATALOG_PAGE);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return router;
    }
}
