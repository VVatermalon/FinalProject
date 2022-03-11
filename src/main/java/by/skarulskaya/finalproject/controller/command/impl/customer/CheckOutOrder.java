package by.skarulskaya.finalproject.controller.command.impl.customer;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Address;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.service.impl.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static by.skarulskaya.finalproject.controller.PagesPaths.CHECK_OUT_ORDER_PAGE;
import static by.skarulskaya.finalproject.controller.PagesPaths.PAYMENT_PAGE;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;

public class CheckOutOrder implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final AddressService addressService = AddressService.getInstance();
    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        Map<String, String> mapData = new HashMap<>();
        HttpSession session = request.getSession();
        Customer customer = (Customer)session.getAttribute(CUSTOMER);
        mapData.put(COUNTRY, request.getParameter(COUNTRY));
        mapData.put(CITY, request.getParameter(CITY));
        mapData.put(ADDRESS, request.getParameter(ADDRESS));
        mapData.put(APARTMENT, request.getParameter(APARTMENT));
        mapData.put(POSTAL_CODE, request.getParameter(POSTAL_CODE));
        String saveAddress = request.getParameter(SAVE_FOR_LATER);
        mapData.put(SAVE_FOR_LATER, saveAddress);
        try {
            Optional<Address> address = addressService.createAddress(mapData, customer.getId());
            if(address.isPresent()) {
                session.setAttribute(ADDRESS, address.get());
                if(saveAddress != null) {
                    customer.setDefaultAddress(address);
                    session.setAttribute(CUSTOMER, customer);
                }
                router.setCurrentPage(PAYMENT_PAGE);
                return router;
            }
            for(String key: mapData.keySet()) {
                String message = mapData.get(key);
                switch(message) {
                    case INVALID_COUNTRY -> {
                        request.setAttribute(INVALID_COUNTRY, INVALID_COUNTRY_MESSAGE);
                        mapData.put(key, null);
                    }
                    case INVALID_CITY -> {
                        request.setAttribute(INVALID_CITY, INVALID_CITY_MESSAGE);
                        mapData.put(key, null);
                    }
                    case INVALID_ADDRESS -> {
                        request.setAttribute(INVALID_ADDRESS, INVALID_ADDRESS_MESSAGE);
                        mapData.put(key, null);
                    }
                    case INVALID_APARTMENT -> {
                        request.setAttribute(INVALID_APARTMENT, INVALID_APARTMENT_MESSAGE);
                        mapData.put(key, null);
                    }
                    case INVALID_POSTAL_CODE -> {
                        request.setAttribute(INVALID_POSTAL_CODE, INVALID_POSTAL_CODE_MESSAGE);
                        mapData.put(key, null);
                    }
                }
            }
            request.setAttribute(ADDRESS_DATA_MAP, mapData);
            router.setCurrentPage(CHECK_OUT_ORDER_PAGE);
            return router;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
