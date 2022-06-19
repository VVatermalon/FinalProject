package by.skarulskaya.finalproject.controller.command.impl.customer;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.OrderComponent;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import by.skarulskaya.finalproject.model.service.impl.OrderComponentService;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static by.skarulskaya.finalproject.controller.PagesPaths.ERROR_500;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.NOT_UNIQUE_PHONE_MESSAGE;

public class AddItemToCart implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final int MIN_AMOUNT = 1;
    private static final OrderComponentService orderComponentService = OrderComponentService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        String currentPage = (String) session.getAttribute(CURRENT_PAGE);
        Map<String, String> mapData = new HashMap<>();
        mapData.put(CART_ORDER_ID, String.valueOf(((int) session.getAttribute(CART_ORDER_ID))));
        mapData.put(ITEM_ID, request.getParameter(ITEM_ID));
        mapData.put(AMOUNT, request.getParameter(AMOUNT));
        mapData.put(SIZE_ID, request.getParameter(SIZE_ID));
        mapData.put(CHANGE_FROM_CART, request.getParameter(CHANGE_FROM_CART));
        try {
            if (!orderComponentService.addItemToCart(mapData)) {
                for (String key : mapData.keySet()) {
                    String message = mapData.get(key);
                    switch (message) {
                        case INVALID_ITEM_ID -> {
                            router.setCurrentPage(ERROR_500);
                            logger.error("Can't find item by id, id = {}", session.getAttribute(ITEM_ID));
                            return router;
                        }
                        case INVALID_SIZE_ID -> {
                            router.setCurrentPage(ERROR_500);
                            logger.error("Can't find size by id, id = {}", session.getAttribute(SIZE_ID));
                            return router;
                        }
                        case ERROR_NOT_ENOUGH_ITEMS_IN_STOCK -> request.setAttribute(ERROR_ADD_ITEM_TO_CART, ERROR_NOT_ENOUGH_ITEMS_IN_STOCK);
                        case ERROR_CANNOT_ADD_MORE_LIMIT -> request.setAttribute(ERROR_ADD_ITEM_TO_CART, ERROR_CANNOT_ADD_MORE_LIMIT);
                        case ERROR_ADD_ITEM_TO_CART -> {
                            router.setCurrentPage(ERROR_500);
                            logger.error("Can't add item to cart");
                            return router;
                        }
                    }
                }
            }
            int itemsInCartCount = orderComponentService.countItemsInCart((int) session.getAttribute(CART_ORDER_ID));
            session.setAttribute(ITEMS_IN_CART_COUNT, itemsInCartCount);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        router.setCurrentPage(currentPage);
        return router;
    }
}
