package by.skarulskaya.finalproject.controller.command.impl.admin;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.CustomEntity;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.service.CategoryService;
import by.skarulskaya.finalproject.model.service.impl.CategoryServiceImpl;
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
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;

public class ChangeCategory implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final CategoryService categoryService = CategoryServiceImpl.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        String categoryIdParameter = request.getParameter(CATEGORY_ID);
        String categoryName = request.getParameter(NEW_CATEGORY_NAME);
        router.setCurrentPage(currentPage);
        try {
            if (!BaseValidatorImpl.getInstance().validateId(categoryIdParameter)) {
                router.setCurrentPage(ERROR_404);
                return router;
            }
            int categoryId = Integer.parseInt(categoryIdParameter);
            if (!BaseValidatorImpl.getInstance().validateCategoryName(categoryName)) {
                request.setAttribute(INVALID_NEW_CATEGORY_NAME, INVALID_CATEGORY_NAME_MESSAGE);
                return router;
            }
            if (!categoryService.isCategoryNameUnique(categoryName, categoryId)) {
                request.setAttribute(INVALID_NEW_CATEGORY_NAME, NOT_UNIQUE_CATEGORY_NAME_MESSAGE);
                return router;
            }
            ItemCategory category = new ItemCategory(categoryId, categoryName);
            if (!categoryService.update(category)) {
                logger.error("Can't update category");
                throw new CommandException("Can't update category");
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
            if(e.getCause().getCause().getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                request.setAttribute(INVALID_CATEGORY, CATEGORY_FOREIGN_KEY_FAILS);
                return router;
            }
            logger.error(e);
            throw new CommandException(e);
        }
    }
}