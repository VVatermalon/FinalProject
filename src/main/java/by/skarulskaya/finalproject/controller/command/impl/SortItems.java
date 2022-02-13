package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.controller.command.CommandType;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.SortOrder;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static by.skarulskaya.finalproject.controller.PagesPaths.CATALOG_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;

public class SortItems implements Command {
    private static final int ITEM_PER_PAGE = 6;
    private static final ItemService itemService = ItemService.getInstance();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        int currentPage = request.getParameter(PAGE) != null ? Integer.parseInt(request.getParameter(PAGE)) : 1; //todo если параметр неправильный
        Item.ItemSortParameter sortParameter = Item.ItemSortParameter.valueOf(request.getParameter(SORT));
        SortOrder sortOrder = SortOrder.valueOf(request.getParameter(SORT_ORDER));
        String category = request.getParameter(CATEGORY_ID);
        List<Item> itemList;
        try {
            if(category == null) {
                itemList = itemService.findAllByPageSort(sortParameter, sortOrder, ITEM_PER_PAGE, (currentPage - 1) * ITEM_PER_PAGE);
                if(itemList.size() == ITEM_PER_PAGE) {
                    request.setAttribute(IS_NEXT_PAGE, true);
                }
                if(itemList.isEmpty() && currentPage > 1){
                    currentPage--;
                    itemList = itemService.findAllByPageSort(sortParameter, sortOrder, ITEM_PER_PAGE, (currentPage - 1) * ITEM_PER_PAGE);
                }
            }
            else {
                int categoryId = Integer.parseInt(category);
                itemList = itemService.findAllByCategoryByPageSort(categoryId, sortParameter, sortOrder, ITEM_PER_PAGE, (currentPage - 1) * ITEM_PER_PAGE);
                if(itemList.size() == ITEM_PER_PAGE) {
                    request.setAttribute(IS_NEXT_PAGE, true);
                }
                if(itemList.isEmpty() && currentPage > 1){
                    currentPage--;
                    itemList = itemService.findAllByCategoryByPageSort(categoryId, sortParameter, sortOrder, ITEM_PER_PAGE, (currentPage - 1) * ITEM_PER_PAGE);
                }
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
