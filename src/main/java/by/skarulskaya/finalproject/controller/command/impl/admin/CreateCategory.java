package by.skarulskaya.finalproject.controller.command.impl.admin;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.CustomEntity;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.service.impl.CategoryService;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static by.skarulskaya.finalproject.controller.PagesPaths.ERROR_404;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.NOT_UNIQUE_IMAGE_NAME_MESSAGE;

public class CreateCategory implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final CategoryService categoryService = CategoryService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        String categoryName = request.getParameter(CATEGORY_NAME);
        router.setCurrentPage(currentPage);
        try {
            if (!BaseValidatorImpl.INSTANCE.validateCategoryName(categoryName)) {
                request.setAttribute(INVALID_CATEGORY_NAME, INVALID_CATEGORY_NAME_MESSAGE);
                return router;
            }
            if (!categoryService.isCategoryNameUnique(categoryName)) {
                request.setAttribute(INVALID_CATEGORY_NAME, NOT_UNIQUE_CATEGORY_NAME_MESSAGE);
                return router;
            }
            if (!categoryService.create(categoryName)) {
                logger.error("Can't create category");
                throw new CommandException("Can't create category");
            }
            List<ItemCategory> categories = categoryService.findAllCategories();
            categories = categories.stream()
                    .sorted(Comparator.comparingInt(CustomEntity::getId))
                    .toList();
            request.getServletContext().setAttribute(CATEGORY_LIST, categories);
            router.setCurrentType(Router.Type.REDIRECT);
            router.setCurrentPage(request.getContextPath() + currentPage);
            return router;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}