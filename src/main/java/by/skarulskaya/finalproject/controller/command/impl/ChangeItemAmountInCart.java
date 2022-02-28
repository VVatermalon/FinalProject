package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.Parameters.CART;
import static by.skarulskaya.finalproject.controller.ParametersMessages.ERROR_CANNOT_ADD_ITEM_TO_CART_NOT_ENOUGH_AMOUNT_MESSAGE;

public class ChangeItemAmountInCart implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String COMMAND_PARAMETER = "/controller?command=upload_cart";
    private static final String ERROR_PARAMETER = "&error_cart=";
    private static final ItemService itemService = ItemService.getInstance();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        HashMap<Map.Entry<Integer, Integer>, Integer> cart = (HashMap<Map.Entry<Integer, Integer>, Integer>) session.getAttribute(CART);
        int itemId = Integer.parseInt(request.getParameter(ITEM_ID));
        int amount = Integer.parseInt(request.getParameter(AMOUNT));
        String sizeParameter = request.getParameter(SIZE_ID);
        StringBuilder page = new StringBuilder(request.getContextPath() + COMMAND_PARAMETER);
        try {
            if (itemService.changeItemAmountInCart(itemId, amount, sizeParameter, cart)) {
                session.setAttribute(CART, cart);
            } else {
                page.append(ERROR_PARAMETER).append(ERROR_CANNOT_ADD_ITEM_TO_CART_NOT_ENOUGH_AMOUNT_MESSAGE);
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        router.setCurrentType(Router.Type.REDIRECT);
        router.setCurrentPage(page.toString());
        return router;
    }
}
