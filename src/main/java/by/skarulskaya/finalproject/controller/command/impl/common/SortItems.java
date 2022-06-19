package by.skarulskaya.finalproject.controller.command.impl.common;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.controller.command.CommandType;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.SortOrder;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import by.skarulskaya.finalproject.util.pagination.Pagination;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import static by.skarulskaya.finalproject.controller.PagesPaths.CATALOG_PAGE;
import static by.skarulskaya.finalproject.controller.PagesPaths.ERROR_404;
import static by.skarulskaya.finalproject.controller.Parameters.*;

public class SortItems implements Command {
    private static final int FIRST_PAGINATION_PAGE = 1;
    private static final int ITEM_PER_PAGE = 6;
    private static final int ITEM_PER_PAGE_ADMIN = 5;
    private static final ItemService itemService = ItemService.getInstance();
    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        User user = (User)request.getSession().getAttribute(USER);
        int itemPerPage = user != null && user.getRole() == User.Role.ADMIN ? ITEM_PER_PAGE_ADMIN : ITEM_PER_PAGE;
        String pageParameter = request.getParameter(PAGE);
        int pagePagination = FIRST_PAGINATION_PAGE;
        if(pageParameter != null) {
            if(!BaseValidatorImpl.INSTANCE.validatePage(pageParameter)) {
                throw new CommandException("Invalid pagination page parameter, page = " + pageParameter);
            }
            pagePagination = Integer.parseInt(pageParameter);
        }
        int offset = Pagination.offset(itemPerPage, pagePagination);
        try {
            String sortParameter = request.getParameter(SORT);
            if(sortParameter == null) {
                router.setCurrentPage(ERROR_404);
                return router;
            }
            Item.ItemSortParameter sort = Item.ItemSortParameter.valueOf(sortParameter);
            SortOrder sortOrder = SortOrder.valueOf(request.getParameter(SORT_ORDER));
            String category = request.getParameter(CATEGORY_ID);
            List<Item> itemList;
            if(category == null) {
                itemList = itemService.findAllByPageSort(sort, sortOrder, itemPerPage, offset);
                if(itemList.size() == itemPerPage) {
                    request.setAttribute(IS_NEXT_PAGE, true);
                }
                if(itemList.isEmpty() && pagePagination > FIRST_PAGINATION_PAGE){
                    pagePagination--;
                    offset = Pagination.offset(itemPerPage, pagePagination);
                    itemList = itemService.findAllByPageSort(sort, sortOrder, itemPerPage, offset);
                }
            }
            else {
                int categoryId = Integer.parseInt(category);
                itemList = itemService.findAllByCategoryByPageSort(categoryId, sort, sortOrder, itemPerPage, offset);
                if(itemList.size() == itemPerPage) {
                    request.setAttribute(IS_NEXT_PAGE, true);
                }
                if(itemList.isEmpty() && pagePagination > FIRST_PAGINATION_PAGE){
                    pagePagination--;
                    offset = (pagePagination - 1) * itemPerPage;
                    itemList = itemService.findAllByCategoryByPageSort(categoryId, sort, sortOrder, itemPerPage, offset);
                }
            }
            request.setAttribute(ITEM_LIST, itemList);
            request.setAttribute(PAGE, pagePagination);
            router.setCurrentPage(CATALOG_PAGE);
        } catch (ServiceException | IllegalArgumentException e) {
            throw new CommandException(e);
        }
        return router;
    }
}
