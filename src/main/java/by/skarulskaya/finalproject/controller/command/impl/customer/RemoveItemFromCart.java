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

public class RemoveItemFromCart implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final OrderComponentService orderComponentService = OrderComponentService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        int cartOrderId = (int) session.getAttribute(CART_ORDER_ID);
        int itemId = Integer.parseInt(request.getParameter(ITEM_ID));
        String sizeParameter = request.getParameter(SIZE_ID);
        int sizeId = sizeParameter == null ? 1 : Integer.parseInt(sizeParameter);
        OrderComponent.OrderComponentKey key = new OrderComponent.OrderComponentKey(cartOrderId, itemId, sizeId);

        try {
            if (!orderComponentService.removeItemFromCart(key)) {
                throw new CommandException("Can't delete product from cart");
            }
            int itemsInCartCount = orderComponentService.countItemsInCart(cartOrderId);
            session.setAttribute(ITEMS_IN_CART_COUNT, itemsInCartCount);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        router.setCurrentType(Router.Type.REDIRECT);
        router.setCurrentPage(request.getContextPath() + "/controller?command=upload_cart");
        return router;
    }
}
