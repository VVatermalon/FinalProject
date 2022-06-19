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

import java.util.HashMap;
import java.util.Map;

import static by.skarulskaya.finalproject.controller.Parameters.*;

public class RemoveItemFromCart implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final OrderComponentService orderComponentService = OrderComponentService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        String currentPage = (String) session.getAttribute(CURRENT_PAGE);
        Map<String, String> mapData = new HashMap<>();
        mapData.put(CART_ORDER_ID, String.valueOf(((int) session.getAttribute(CART_ORDER_ID))));
        mapData.put(ITEM_ID, request.getParameter(ITEM_ID));
        mapData.put(SIZE_ID, request.getParameter(SIZE_ID));
        try {
            if (!orderComponentService.removeItemFromCart(mapData)) {
                throw new CommandException("Can't delete item from cart, item id = " + request.getParameter(ITEM_ID));
            }
            int itemsInCartCount = orderComponentService.countItemsInCart((int) session.getAttribute(CART_ORDER_ID));
            session.setAttribute(ITEMS_IN_CART_COUNT, itemsInCartCount);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        router.setCurrentType(Router.Type.REDIRECT);
        router.setCurrentPage(request.getContextPath() + currentPage);
        return router;
    }
}
