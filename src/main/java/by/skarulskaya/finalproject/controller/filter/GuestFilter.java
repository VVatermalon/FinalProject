package by.skarulskaya.finalproject.controller.filter;

import by.skarulskaya.finalproject.model.entity.*;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

import static by.skarulskaya.finalproject.controller.Parameters.*;

public class GuestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER);
        Customer customer = (Customer) session.getAttribute(CUSTOMER);
        if(user == null && customer == null) {
            user = new User();
            user.setRole(User.Role.GUEST);
            session.setAttribute(USER, user);
            session.setAttribute(CART, new HashMap<Map.Entry<Integer, Integer>, Integer>());
//            Order order = new Order();
//            Cookie[] cookies = request.getCookies();
//            if(cookies !=null) {
//                Optional<Cookie> optionalCookie =  Arrays.stream(cookies).
//                        filter(cookie -> cookie.getName().equals("ORDER_COMPONENTS")).findFirst();
//                if(optionalCookie.isPresent()) {
//                    Cookie cookie = optionalCookie.get();
//                    String components = cookie.getValue();
//                }
//            }
            //todo order into cookies
        }
        filterChain.doFilter(request,response);
    }
}
