package by.skarulskaya.finalproject.controller.command.impl.customer;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.service.CustomerService;
import by.skarulskaya.finalproject.model.service.impl.CustomerServiceImpl;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.ERROR_CANNOT_ADD_MONEY_OVER_LIMIT;

public class AddMoneyToAccount implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final CustomerService customerService = CustomerServiceImpl.getInstance();
    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        String currentPage = (String) session.getAttribute(CURRENT_PAGE);
        String moneyParameter = request.getParameter(MONEY);
        if(!BaseValidatorImpl.getInstance().validateMoney(moneyParameter)) {
            logger.error("Invalid money parameter");
            throw new CommandException("Invalid money parameter, money = " + moneyParameter);
        }
        double money = Double.parseDouble(moneyParameter);
        Customer customer = (Customer) session.getAttribute(CUSTOMER);
        try {
            BigDecimal expectedAmount = customer.getBankAccount().add(BigDecimal.valueOf(money));
            boolean isUpdated = customerService.addMoneyToAccount(money, customer);
            if(!isUpdated || customer.getBankAccount().compareTo(expectedAmount) != 0) {
                router.setCurrentPage(currentPage);
                request.setAttribute(ERROR_ADD_MONEY, ERROR_CANNOT_ADD_MONEY_OVER_LIMIT);
            }
            else {
                router.setCurrentType(Router.Type.REDIRECT);
                router.setCurrentPage(request.getContextPath() + currentPage);
            }
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        return router;
    }
}
