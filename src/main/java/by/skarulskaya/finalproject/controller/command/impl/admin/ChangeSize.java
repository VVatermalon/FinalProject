package by.skarulskaya.finalproject.controller.command.impl.admin;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.CustomEntity;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.entity.ItemSize;
import by.skarulskaya.finalproject.model.service.impl.CategoryService;
import by.skarulskaya.finalproject.model.service.impl.ItemSizeService;
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
import static by.skarulskaya.finalproject.controller.Parameters.INVALID_CATEGORY;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;

public class ChangeSize implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final ItemSizeService sizeService = ItemSizeService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        String sizeIdParameter = request.getParameter(SIZE_ID);
        String sizeName = request.getParameter(NEW_SIZE_NAME);
        router.setCurrentPage(currentPage);
        try {
            if (!BaseValidatorImpl.INSTANCE.validateId(sizeIdParameter)) {
                router.setCurrentPage(ERROR_404);
                return router;
            }
            int sizeId = Integer.parseInt(sizeIdParameter);
            if (!BaseValidatorImpl.INSTANCE.validateSizeName(sizeName)) {
                request.setAttribute(INVALID_NEW_SIZE_NAME, INVALID_SIZE_NAME_MESSAGE);
                return router;
            }
            if (!sizeService.isSizeNameUnique(sizeName, sizeId)) {
                request.setAttribute(INVALID_NEW_SIZE_NAME, NOT_UNIQUE_SIZE_NAME_MESSAGE);
                return router;
            }
            ItemSize size = new ItemSize(sizeId, sizeName);
            if (!sizeService.update(size)) {
                logger.error("Can't update size");
                throw new CommandException("Can't update size");
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
                request.setAttribute(INVALID_SIZE, SIZE_FOREIGN_KEY_FAILS);
                return router;
            }
            throw new CommandException(e);
        }
    }
}