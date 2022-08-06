package by.skarulskaya.finalproject.controller.filter;

import by.skarulskaya.finalproject.controller.command.CommandType;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.service.OrderComponentService;
import by.skarulskaya.finalproject.model.service.impl.OrderComponentServiceImpl;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;

import static by.skarulskaya.finalproject.controller.PagesPaths.*;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.Parameters.CART_TOTAL_PRICE;

@WebFilter(filterName = "checkOrderBeforePayment", urlPatterns = "/*")
public class CheckOrderBeforePayment implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private final OrderComponentService orderComponentService = OrderComponentServiceImpl.getInstance();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String command = request.getParameter(COMMAND);
        String requestURI = request.getServletPath();
        if (!requestURI.contains(PAYMENT_PAGE) && (command == null || !command.equalsIgnoreCase(CommandType.PAY_ORDER.name()))){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        int cartOrderId = (int) session.getAttribute(CART_ORDER_ID);
        try {
            BigDecimal totalPrice = orderComponentService.findCartTotalPrice(cartOrderId);
            session.setAttribute(CART_TOTAL_PRICE, totalPrice);
            if (totalPrice.compareTo(BigDecimal.ZERO) == 0) {
                logger.error("Can't pay order: total price equals 0");
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            if (session.getAttribute(ADDRESS_ID) == null) {
                logger.warn("Can't pay order: shipping address is null");
                response.sendRedirect(request.getContextPath() + ADD_SHIPPING_ADDRESS_PAGE);
                return;
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
