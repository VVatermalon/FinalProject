package by.skarulskaya.finalproject.controller.command.impl.admin;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Order;
import by.skarulskaya.finalproject.model.service.impl.CustomerService;
import by.skarulskaya.finalproject.model.service.impl.OrderService;
import by.skarulskaya.finalproject.util.pagination.Pagination;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static by.skarulskaya.finalproject.controller.PagesPaths.ORDERS_PAGE;
import static by.skarulskaya.finalproject.controller.PagesPaths.ORDER_HISTORY_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;

public class UploadOrders implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final int FIRST_PAGINATION_PAGE = 1;
    private static final int ORDER_PER_PAGE = 8;
    private static final OrderService orderService = OrderService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String pageParameter = request.getParameter(PAGE);
        int pagePagination = FIRST_PAGINATION_PAGE;
        if(pageParameter != null) {
            if(!BaseValidatorImpl.INSTANCE.validatePage(pageParameter)) {
                throw new CommandException("Invalid pagination page parameter, page = " + pageParameter);
            }
            pagePagination = Integer.parseInt(pageParameter);
        }
        int offset = Pagination.offset(ORDER_PER_PAGE, pagePagination);
        try {
            List<Order> orders = orderService.findAllRegisteredOrdersByPage(ORDER_PER_PAGE, offset);
            if(orders.size() == ORDER_PER_PAGE) {
                request.setAttribute(IS_NEXT_PAGE, true);
            }
            if(orders.isEmpty() && pagePagination > FIRST_PAGINATION_PAGE){
                pagePagination--;
                offset = Pagination.offset(ORDER_PER_PAGE, pagePagination);
                orders = orderService.findAllRegisteredOrdersByPage(ORDER_PER_PAGE, offset);
            }
            orders.sort(null);
            request.setAttribute(ORDERS, orders);
            request.setAttribute(PAGE, pagePagination);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        router.setCurrentPage(ORDERS_PAGE);
        return router;
    }
}