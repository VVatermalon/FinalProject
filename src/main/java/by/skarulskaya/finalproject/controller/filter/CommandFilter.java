package by.skarulskaya.finalproject.controller.filter;

import by.skarulskaya.finalproject.controller.command.CommandType;
import by.skarulskaya.finalproject.controller.filter.access.CommandAccess;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import static by.skarulskaya.finalproject.controller.PagesPaths.ERROR_404;
import static by.skarulskaya.finalproject.controller.Parameters.*;

@WebFilter(filterName = "commandFilter", urlPatterns = "/controller")
public class CommandFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String command = request.getParameter(COMMAND);
        if (command == null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        User.Role role = User.Role.GUEST;
        Set<String> commands;
        User user = (User) session.getAttribute(USER);
        Customer customer = (Customer) session.getAttribute(CUSTOMER);
        if(user != null){
            role = user.getRole();
        }
        if(customer != null){
            role = customer.getRole();
        }
        switch (role){
            case ADMIN -> {
                commands = CommandAccess.ADMIN.getCommands();
            }
            case CUSTOMER -> {
                commands = CommandAccess.CUSTOMER.getCommands();
            }
            default -> {
                commands = CommandAccess.GUEST.getCommands();
            }
        }
        boolean isCorrect = Arrays.stream(CommandType.values())
                .anyMatch(commandType -> command.equalsIgnoreCase(commandType.toString()));
        if(!isCorrect) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if(commands.contains(command.toUpperCase())){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
