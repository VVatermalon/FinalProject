package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemSize;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import by.skarulskaya.finalproject.model.service.impl.ItemSizeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static by.skarulskaya.finalproject.controller.PagesPaths.CART_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.Parameters.CART;

public class RemoveItemFromCart implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final ItemService itemService = ItemService.getInstance();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        HashMap<Map.Entry<Integer, Integer>, Integer> cart = (HashMap<Map.Entry<Integer, Integer>, Integer>) session.getAttribute(CART);
        int itemId = Integer.parseInt(request.getParameter(ITEM_ID));
        String sizeParameter = request.getParameter(SIZE_ID);
        if (itemService.removeItemFromCart(itemId, sizeParameter, cart)) {
            session.setAttribute(CART, cart);
        } else {
            throw new CommandException("Can't delete product from cart");
        }
        router.setCurrentType(Router.Type.REDIRECT);
        router.setCurrentPage(request.getContextPath()+"/controller?command=upload_cart");
        return router;
    }
}
