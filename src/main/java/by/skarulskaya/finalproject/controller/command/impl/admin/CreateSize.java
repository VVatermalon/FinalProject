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

import java.util.Comparator;
import java.util.List;

import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;

public class CreateSize implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final ItemSizeService sizeService = ItemSizeService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        String sizeName = request.getParameter(SIZE_NAME);
        router.setCurrentPage(currentPage);
        try {
            if (!BaseValidatorImpl.INSTANCE.validateSizeName(sizeName)) {
                request.setAttribute(INVALID_SIZE_NAME, INVALID_SIZE_NAME_MESSAGE);
                return router;
            }
            if (!sizeService.isSizeNameUnique(sizeName)) {
                request.setAttribute(INVALID_SIZE_NAME, NOT_UNIQUE_SIZE_NAME_MESSAGE);
                return router;
            }
            if (!sizeService.create(sizeName)) {
                logger.error("Can't create size");
                throw new CommandException("Can't create size");
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
            throw new CommandException(e);
        }
    }
}