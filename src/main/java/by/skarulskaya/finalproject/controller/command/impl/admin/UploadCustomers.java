package by.skarulskaya.finalproject.controller.command.impl.admin;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.service.CustomerService;
import by.skarulskaya.finalproject.model.service.impl.CustomerServiceImpl;
import by.skarulskaya.finalproject.util.pagination.Pagination;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static by.skarulskaya.finalproject.controller.PagesPaths.USERS_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.Parameters.PAGE;

public class UploadCustomers implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final int FIRST_PAGINATION_PAGE = 1;
    private static final int USER_PER_PAGE = 8;
    private final CustomerService customerService = CustomerServiceImpl.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String pageParameter = request.getParameter(PAGE);
        int pagePagination = FIRST_PAGINATION_PAGE;
        if(pageParameter != null) {
            if(!BaseValidatorImpl.getInstance().validatePage(pageParameter)) {
                logger.error("Invalid pagination page parameter");
                throw new CommandException("Invalid pagination page parameter, page = " + pageParameter);
            }
            pagePagination = Integer.parseInt(pageParameter);
        }
        int offset = Pagination.offset(USER_PER_PAGE, pagePagination);
        String userStatus = request.getParameter(USER_STATUS);
        try {
            List<Customer> customers = customerService.findAllByStatusByPage(userStatus, USER_PER_PAGE, offset);
            if(customers.size() == USER_PER_PAGE) {
                request.setAttribute(IS_NEXT_PAGE, true);
            }
            if(customers.isEmpty() && pagePagination > FIRST_PAGINATION_PAGE){
                pagePagination--;
                offset = Pagination.offset(USER_PER_PAGE, pagePagination);
                customers = customerService.findAllByStatusByPage(userStatus, USER_PER_PAGE, offset);
            }
            customers.sort(null);
            request.setAttribute(USER_LIST, customers);
            request.setAttribute(PAGE, pagePagination);
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        router.setCurrentPage(USERS_PAGE);
        return router;
    }
}
