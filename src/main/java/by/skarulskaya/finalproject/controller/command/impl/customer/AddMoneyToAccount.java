package by.skarulskaya.finalproject.controller.command.impl.customer;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.service.impl.CustomerService;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;

import static by.skarulskaya.finalproject.controller.PagesPaths.BANK_ACCOUNT_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.ERROR_CANNOT_ADD_MONEY_OVER_LIMIT;

public class AddMoneyToAccount implements Command {
    private final CustomerService customerService = CustomerService.getInstance();
    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        String currentPage = (String) session.getAttribute(CURRENT_PAGE);
        String moneyParameter = request.getParameter(MONEY);
        if(!BaseValidatorImpl.INSTANCE.validateMoney(moneyParameter)) {
            throw new CommandException("Invalid money parameter, money = " + moneyParameter);
        }
        double money = Double.parseDouble(moneyParameter);
        Customer customer = (Customer) session.getAttribute(CUSTOMER);
        try {
            BigDecimal expectedAmount = customer.getBankAccount().add(BigDecimal.valueOf(money));
            boolean isUpdated = customerService.addMoneyToAccount(money, customer);
            if(!isUpdated || customer.getBankAccount().compareTo(expectedAmount) != 0) {
                request.setAttribute(ERROR_ADD_MONEY, ERROR_CANNOT_ADD_MONEY_OVER_LIMIT);
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        router.setCurrentPage(currentPage);
        return router;
    }
}
