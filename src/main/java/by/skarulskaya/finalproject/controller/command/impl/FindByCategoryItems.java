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
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.Parameters.PAGE;

public class FindByCategoryItems implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final int ITEM_PER_PAGE = 6;
    private static final ItemService itemService = ItemService.getInstance();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        int currentPage = request.getParameter(PAGE) != null ? Integer.parseInt(request.getParameter(PAGE)) : 1;
        int offset = (currentPage - 1) * ITEM_PER_PAGE;
        int categoryId = Integer.parseInt(request.getParameter(CATEGORY_ID));
        try {
            List<Item> itemList = itemService.findAllByCategoryByPage(categoryId, ITEM_PER_PAGE, offset);
            if(itemList.size() == ITEM_PER_PAGE) {
                request.setAttribute(IS_NEXT_PAGE, true);
            }
            if(itemList.isEmpty() && currentPage > 1){
                currentPage--;
                offset = (currentPage - 1) * ITEM_PER_PAGE;
                itemList = itemService.findAllByCategoryByPage(categoryId, ITEM_PER_PAGE, offset);
            }
            if(itemList.isEmpty()) {
                request.setAttribute(NO_ITEMS, NO_ITEMS);
            }
            request.setAttribute(ITEM_LIST, itemList);
            request.setAttribute(PAGE, currentPage);
            //request.setAttribute(CATEGORY_ID, categoryId);
            router.setCurrentPage(CATALOG_PAGE);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return router;
    }
}
