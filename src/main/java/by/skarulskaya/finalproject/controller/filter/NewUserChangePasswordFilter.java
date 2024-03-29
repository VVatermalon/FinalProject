package by.skarulskaya.finalproject.controller.filter;

import by.skarulskaya.finalproject.controller.command.CommandType;
import by.skarulskaya.finalproject.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static by.skarulskaya.finalproject.controller.PagesPaths.*;
import static by.skarulskaya.finalproject.controller.Parameters.*;

@WebFilter(filterName = "newUserChangePasswordFilter", urlPatterns = "/*")
public class NewUserChangePasswordFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER);
        String requestURI = request.getServletPath();
        String command = request.getParameter(COMMAND);
        if(user != null && user.getRole() == User.Role.ADMIN && user.getStatus() == User.Status.IN_REGISTRATION_PROCESS) {
            if (requestURI.contains(SETTINGS_PAGE) || (command != null
                    && (command.equalsIgnoreCase(CommandType.CHANGE_SETTING.name())
                    || command.equalsIgnoreCase(CommandType.SIGN_OUT.name())))){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            logger.warn("Need to change password");
            response.sendRedirect(request.getContextPath() + SETTINGS_PAGE);
            return;
        }
        filterChain.doFilter(request, response);
    }
}

