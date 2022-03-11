package by.skarulskaya.finalproject.controller.command.impl.customer;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.OrderComponent;
import by.skarulskaya.finalproject.model.service.impl.OrderComponentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.ERROR_CANNOT_ADD_ITEM_TO_CART_NOT_ENOUGH_AMOUNT_MESSAGE;

public class ChangeItemAmountInCart implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String COMMAND_PARAMETER = "/controller?command=upload_cart";
    private static final String ERROR_PARAMETER = "&error_cart=";
    private static final OrderComponentService orderComponentService = OrderComponentService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        int cartOrderId = (int) session.getAttribute(CART_ORDER_ID);
        int itemId = Integer.parseInt(request.getParameter(ITEM_ID));
        int amount = Integer.parseInt(request.getParameter(AMOUNT));
        String sizeParameter = request.getParameter(SIZE_ID);
        int sizeId = sizeParameter == null ? 1 : Integer.parseInt(sizeParameter);
        OrderComponent.OrderComponentKey key = new OrderComponent.OrderComponentKey(cartOrderId, itemId, sizeId);
        StringBuilder page = new StringBuilder(request.getContextPath() + COMMAND_PARAMETER);

        try {
            if (!orderComponentService.changeItemAmountInCart(key, amount)) {
                page.append(ERROR_PARAMETER).append(ERROR_CANNOT_ADD_ITEM_TO_CART_NOT_ENOUGH_AMOUNT_MESSAGE);
            }
            int itemsInCartCount = orderComponentService.countItemsInCart(cartOrderId);
            session.setAttribute(ITEMS_IN_CART_COUNT, itemsInCartCount);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        router.setCurrentType(Router.Type.REDIRECT);
        router.setCurrentPage(page.toString());
        return router;
    }
}
