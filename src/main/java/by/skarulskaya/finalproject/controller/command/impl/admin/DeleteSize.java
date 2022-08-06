package by.skarulskaya.finalproject.controller.command.impl.admin;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.CustomEntity;
import by.skarulskaya.finalproject.model.entity.ItemSize;
import by.skarulskaya.finalproject.model.service.ItemSizeService;
import by.skarulskaya.finalproject.model.service.impl.ItemSizeServiceImpl;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Comparator;
import java.util.List;

import static by.skarulskaya.finalproject.controller.PagesPaths.ERROR_404;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.SIZE_FOREIGN_KEY_FAILS;

public class DeleteSize implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final ItemSizeService sizeService = ItemSizeServiceImpl.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        String sizeIdParameter = request.getParameter(SIZE_ID);
        router.setCurrentPage(currentPage);
        try {
            if (!BaseValidatorImpl.getInstance().validateId(sizeIdParameter)) {
                router.setCurrentPage(ERROR_404);
                return router;
            }
            int sizeId = Integer.parseInt(sizeIdParameter);
            if (!sizeService.delete(sizeId)) {
                logger.error("Can't delete size");
                throw new CommandException("Can't delete size");
            }
            List<ItemSize> sizes = sizeService.findAll();
            sizes = sizes.stream()
                    .sorted(Comparator.comparingInt(CustomEntity::getId))
                    .toList();
            request.getServletContext().setAttribute(SIZE_LIST, sizes);
            router.setCurrentType(Router.Type.REDIRECT);
            router.setCurrentPage(request.getContextPath() + currentPage);
            return router;
        } catch (ServiceException e) {
            if(e.getCause().getCause().getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                request.setAttribute(INVALID_DELETE_SIZE, SIZE_FOREIGN_KEY_FAILS);
                return router;
            }
            logger.error(e);
            throw new CommandException(e);
        }
    }
}
