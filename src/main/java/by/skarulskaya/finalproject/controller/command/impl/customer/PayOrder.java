package by.skarulskaya.finalproject.controller.command.impl.customer;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.service.impl.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

import static by.skarulskaya.finalproject.controller.PagesPaths.*;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;

public class PayOrder implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final OrderService orderService = OrderService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute(CUSTOMER);
        int cartOrderId = (int) session.getAttribute(CART_ORDER_ID);
        int addressId = (int) session.getAttribute(ADDRESS_ID);
        BigDecimal cartTotalPrice = (BigDecimal) session.getAttribute(CART_TOTAL_PRICE);
        BigDecimal bankAccount = customer.getBankAccount();

        if(bankAccount.compareTo(cartTotalPrice) < 0) {
            request.setAttribute(ERROR_PAY_ORDER, ERROR_NOT_ENOUGH_MONEY);
            router.setCurrentPage(PAYMENT_PAGE);
            return router;
        }
        try {
            if (!orderService.registerOrder(customer, cartOrderId, addressId, cartTotalPrice)) {
                router.setCurrentType(Router.Type.REDIRECT);
                router.setCurrentPage(request.getContextPath() + "/controller?command=upload_cart&error_cart=" + ERROR_CART_WAS_CHANGED_MESSAGE);
                return router;
            }
            int newCartOrderId = orderService.createCart(customer.getId());
            session.setAttribute(CART_ORDER_ID, newCartOrderId);
            session.setAttribute(ITEMS_IN_CART_COUNT, 0);
            session.setAttribute(CUSTOMER, customer);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        router.setCurrentPage(ORDER_SUCCESS_PAGE);
        return router;
    }
}
