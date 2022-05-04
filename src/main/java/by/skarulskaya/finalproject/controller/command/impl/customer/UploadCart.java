package by.skarulskaya.finalproject.controller.command.impl.customer;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.OrderComponent;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import by.skarulskaya.finalproject.model.service.impl.OrderComponentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static by.skarulskaya.finalproject.controller.PagesPaths.CART_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.ERROR_CART_WAS_CHANGED_MESSAGE;

public class UploadCart implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final OrderComponentService orderComponentService = OrderComponentService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        int cartOrderId = (int) session.getAttribute(CART_ORDER_ID);
        try {
            ArrayList<OrderComponent> uploadedCart = new ArrayList<>();
            if (orderComponentService.uploadCart(cartOrderId, uploadedCart)) {
                request.setAttribute(ERROR_CART, ERROR_CART_WAS_CHANGED_MESSAGE);
            }
            uploadedCart.sort(Comparator.comparing(component -> component.getItem().getName()));
            request.setAttribute(UPLOADED_CART, uploadedCart);
            int itemsInCartCount = uploadedCart.stream().mapToInt(OrderComponent::getAmount).sum();
            session.setAttribute(ITEMS_IN_CART_COUNT, itemsInCartCount);
            BigDecimal totalPrice = uploadedCart.stream()
                    .map(component -> component.getItem().getPrice().multiply(BigDecimal.valueOf(component.getAmount())))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            session.setAttribute(CART_TOTAL_PRICE, totalPrice);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        router.setCurrentPage(CART_PAGE);
        return router;
    }
}
