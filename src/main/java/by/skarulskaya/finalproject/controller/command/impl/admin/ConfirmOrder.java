package by.skarulskaya.finalproject.controller.command.impl.admin;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.service.OrderService;
import by.skarulskaya.finalproject.model.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.skarulskaya.finalproject.controller.PagesPaths.ERROR_404;
import static by.skarulskaya.finalproject.controller.Parameters.*;

public class ConfirmOrder implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final OrderService orderService = OrderServiceImpl.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        try {
            String orderIdParameter = request.getParameter(ORDER_ID);
            if(orderIdParameter == null) {
                router.setCurrentPage(ERROR_404);
                return router;
            }
            int orderId = Integer.parseInt(orderIdParameter);
            if(!orderService.confirmOrder(orderId)) {
                logger.error("Can't confirm order");
                throw new CommandException("Can't confirm order, order id = " + orderId);
            }
        } catch (ServiceException | NumberFormatException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        router.setCurrentType(Router.Type.REDIRECT);
        router.setCurrentPage(request.getContextPath() + currentPage);
        return router;
    }
}
