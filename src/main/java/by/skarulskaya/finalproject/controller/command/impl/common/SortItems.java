package by.skarulskaya.finalproject.controller.command.impl.common;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.controller.command.CommandType;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.SortOrder;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import static by.skarulskaya.finalproject.controller.PagesPaths.CATALOG_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;

public class SortItems implements Command {
    private static final int ITEM_PER_PAGE = 6;
    private static final ItemService itemService = ItemService.getInstance();
    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        int currentPage = request.getParameter(PAGE) != null ? Integer.parseInt(request.getParameter(PAGE)) : 1; //todo если параметр неправильный
        int offset = (currentPage - 1) * ITEM_PER_PAGE;
        Item.ItemSortParameter sortParameter = Item.ItemSortParameter.valueOf(request.getParameter(SORT));
        SortOrder sortOrder = SortOrder.valueOf(request.getParameter(SORT_ORDER));
        String category = request.getParameter(CATEGORY_ID);
        List<Item> itemList;
        try {
            if(category == null) {
                itemList = itemService.findAllByPageSort(sortParameter, sortOrder, ITEM_PER_PAGE, offset);
                if(itemList.size() == ITEM_PER_PAGE) {
                    request.setAttribute(IS_NEXT_PAGE, true);
                }
                if(itemList.isEmpty() && currentPage > 1){
                    currentPage--;
                    offset = (currentPage - 1) * ITEM_PER_PAGE;
                    itemList = itemService.findAllByPageSort(sortParameter, sortOrder, ITEM_PER_PAGE, offset);
                }
            }
            else {
                int categoryId = Integer.parseInt(category);
                itemList = itemService.findAllByCategoryByPageSort(categoryId, sortParameter, sortOrder, ITEM_PER_PAGE, offset);
                if(itemList.size() == ITEM_PER_PAGE) {
                    request.setAttribute(IS_NEXT_PAGE, true);
                }
                if(itemList.isEmpty() && currentPage > 1){
                    currentPage--;
                    offset = (currentPage - 1) * ITEM_PER_PAGE;
                    itemList = itemService.findAllByCategoryByPageSort(categoryId, sortParameter, sortOrder, ITEM_PER_PAGE, offset);
                }
            }
            request.setAttribute(ITEM_LIST, itemList);
            request.setAttribute(PAGE, currentPage);
            router.setCurrentPage(CATALOG_PAGE);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return router;
    }
}
