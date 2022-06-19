package by.skarulskaya.finalproject.controller.filter;

import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.CustomEntity;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.service.impl.CategoryService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static by.skarulskaya.finalproject.controller.PagesPaths.ERROR_404;
import static by.skarulskaya.finalproject.controller.Parameters.CATEGORY_LIST;

@WebFilter(filterName = "categoriesFilter", urlPatterns = "/*")
public class CategoriesFilter implements Filter {
    private static final CategoryService categoryService = CategoryService.getInstance();
    private static final Logger logger = LogManager.getLogger();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        if(session.getAttribute(CATEGORY_LIST) == null) {
            logger.debug("Filter would add categories");
            try {
                List<ItemCategory> categories = categoryService.findAllCategories();
                categories.removeIf(c -> c.getId() == 1);
                categories = categories.stream()
                        .filter(c -> c.getId() != 1)
                        .sorted(Comparator.comparingInt(CustomEntity::getId))
                        .toList();
                session.setAttribute(CATEGORY_LIST, categories);
            }
            catch (ServiceException e) {
                ((HttpServletResponse)servletResponse).sendRedirect(ERROR_404); //todo добавить описание ошибки
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
