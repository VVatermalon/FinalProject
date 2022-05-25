package by.skarulskaya.finalproject.controller.command.impl.customer;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.Order;
import by.skarulskaya.finalproject.model.service.impl.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static by.skarulskaya.finalproject.controller.PagesPaths.CART_PAGE;
import static by.skarulskaya.finalproject.controller.PagesPaths.ORDER_HISTORY_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;

public class UploadOrderHistory implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final OrderService orderService = OrderService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute(CUSTOMER);
        try {
            List<Order> orders = orderService.findAllRegisteredOrders(customer.getId());
            orders.sort((o1, o2) -> {
                int dateComparing = o1.getDateOrdered().compareTo(o2.getDateOrdered());
                if(dateComparing == 0) {
                    return -Integer.compare(o1.getId(), o2.getId());
                }
                return -dateComparing;
            });
            request.setAttribute(ORDERS, orders);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        router.setCurrentPage(ORDER_HISTORY_PAGE);
        return router;
    }
}