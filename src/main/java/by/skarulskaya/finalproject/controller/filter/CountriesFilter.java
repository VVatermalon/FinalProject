package by.skarulskaya.finalproject.controller.filter;

import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Address;
import by.skarulskaya.finalproject.model.entity.CustomEntity;
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
import static by.skarulskaya.finalproject.controller.Parameters.COUNTRY_LIST;

@WebFilter(filterName = "countriesFilter", urlPatterns = "/*")
public class CountriesFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ServletContext context = request.getServletContext();
        if(context.getAttribute(COUNTRY_LIST) != null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        List<String> countryList = Arrays.stream(Address.AVAILABLE_COUNTRIES.values())
                .map(Address.AVAILABLE_COUNTRIES::toString)
                .sorted()
                .toList();
        context.setAttribute(COUNTRY_LIST, countryList);
        filterChain.doFilter(servletRequest, servletResponse);
        logger.info("CountriesFilter added country list");
    }
}
