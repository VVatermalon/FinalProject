package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemSize;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import by.skarulskaya.finalproject.model.service.impl.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.Parameters.CART;
import static by.skarulskaya.finalproject.controller.ParametersMessages.ERROR_CANNOT_ADD_ITEM_TO_CART_NOT_ENOUGH_AMOUNT_MESSAGE;

public class CheckOutOrder implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final OrderService orderService = OrderService.getInstance();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        HashMap<Map.Entry<Item, ItemSize>, Integer> cart = (HashMap<Map.Entry<Item, ItemSize>, Integer>) session.getAttribute(CART);
        String currentPage = (String) session.getAttribute(PAGE);

//        try {
//            if(orderService.checkItemsInCart(cart)) {
//
//            }
//        } catch (ServiceException e) {
//            throw new CommandException(e);
//        }
        session.setAttribute(CART, cart);
        router.setCurrentType(Router.Type.REDIRECT);
        router.setCurrentPage(currentPage);
        return router;
    }
}
