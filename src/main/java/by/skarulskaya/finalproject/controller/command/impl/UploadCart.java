package by.skarulskaya.finalproject.controller.command.impl;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.OrderComponent;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static by.skarulskaya.finalproject.controller.PagesPaths.CART_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.ERROR_CART_WAS_CHANGED_MESSAGE;

public class UploadCart implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final ItemService itemService = ItemService.getInstance();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        HashMap<Map.Entry<Integer, Integer>, Integer> cart = (HashMap<Map.Entry<Integer, Integer>, Integer>) session.getAttribute(CART);
        try {
            ArrayList<OrderComponent> uploadedCart = new ArrayList<>();
            if(itemService.uploadCart(cart, uploadedCart)) {
                request.setAttribute(ERROR_CART, ERROR_CART_WAS_CHANGED_MESSAGE);
            }
            request.setAttribute(UPLOADED_CART, uploadedCart);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        router.setCurrentPage(CART_PAGE);
        return router;
    }
}
