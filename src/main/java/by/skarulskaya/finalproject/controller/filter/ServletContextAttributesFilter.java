package by.skarulskaya.finalproject.controller.filter;

import by.skarulskaya.finalproject.model.entity.Address;
import by.skarulskaya.finalproject.model.entity.Order;
import by.skarulskaya.finalproject.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static by.skarulskaya.finalproject.controller.Parameters.*;

@WebFilter(filterName = "servletContextAttributesFilter", urlPatterns = "/*")
public class ServletContextAttributesFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ServletContext context = request.getServletContext();
        if(context.getAttribute(COUNTRY_LIST) == null) {
            List<String> countryList = Arrays.stream(Address.AvailableCountries.values())
                    .map(Address.AvailableCountries::toString)
                    .sorted()
                    .toList();
            context.setAttribute(COUNTRY_LIST, countryList);
            logger.info("ServletContextAttributesFilter added country list");
        }
        if(context.getAttribute(ORDER_STATUS_LIST) == null) {
            List<String> orderStatusList = Arrays.stream(Order.OrderStatus.values())
                    .filter(status -> status != Order.OrderStatus.IN_PROCESS)
                    .map(Order.OrderStatus::toString)
                    .sorted()
                    .toList();
            context.setAttribute(ORDER_STATUS_LIST, orderStatusList);
            logger.info("ServletContextAttributesFilter added order status list");
        }
        if(context.getAttribute(USER_STATUS_LIST) == null) {
            List<String> userStatusList = Arrays.stream(User.Status.values())
                    .map(User.Status::toString)
                    .sorted()
                    .toList();
            context.setAttribute(USER_STATUS_LIST, userStatusList);
            logger.info("ServletContextAttributesFilter added user status list");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
