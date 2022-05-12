package by.skarulskaya.finalproject.controller.command.impl.customer;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.OrderComponent;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import by.skarulskaya.finalproject.model.service.impl.OrderComponentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;

import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.ERROR_CANNOT_ADD_ITEM_TO_CART_NOT_ENOUGH_AMOUNT_MESSAGE;

public class AddItemToCart implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String AMPERSAND = "&";
    private static final String ERROR_PARAMETER = "&error_cannot_add_item_to_cart=";
    private static final String AMOUNT_PARAMETER = "&amount=";
    private static final String SIZE_ID_PARAMETER = "&size_id=";
    private static final OrderComponentService orderComponentService = OrderComponentService.getInstance();
    private static final ItemService itemService = ItemService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        String currentPage = (String) session.getAttribute(PAGE);
        String[] parameters = currentPage.split(AMPERSAND);
        StringBuilder newCurrentPage = new StringBuilder(100);
        newCurrentPage.append(parameters[0]).append(AMPERSAND).append(parameters[1]);
        int cartOrderId = (int) session.getAttribute(CART_ORDER_ID);
        int itemId = Integer.parseInt(request.getParameter(ITEM_ID));
        int amount = Integer.parseInt(request.getParameter(AMOUNT));
        String sizeParameter = request.getParameter(SIZE_ID);
        int sizeId = sizeParameter == null ? 1 : Integer.parseInt(sizeParameter);
        OrderComponent.OrderComponentKey key = new OrderComponent.OrderComponentKey(cartOrderId, itemId, sizeId);
        try {
            itemService.updatePopularity(itemId);
            if (orderComponentService.addItemToCart(key, amount) != amount) {
                newCurrentPage.append(ERROR_PARAMETER).append(ERROR_CANNOT_ADD_ITEM_TO_CART_NOT_ENOUGH_AMOUNT_MESSAGE);
            }
            int itemsInCartCount = orderComponentService.countItemsInCart(cartOrderId);
            session.setAttribute(ITEMS_IN_CART_COUNT, itemsInCartCount);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        router.setCurrentType(Router.Type.REDIRECT);
        newCurrentPage.append(AMOUNT_PARAMETER).append(amount).append(SIZE_ID_PARAMETER).append(sizeParameter);
        router.setCurrentPage(newCurrentPage.toString());
        return router;
    }
}
