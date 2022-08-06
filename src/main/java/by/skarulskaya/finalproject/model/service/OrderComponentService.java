package by.skarulskaya.finalproject.model.service;

import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.OrderComponent;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderComponentService {
    boolean uploadCart(int cartOrderId, List<OrderComponent> uploadedCart) throws ServiceException;
    List<OrderComponent> findAllOrderComponents(int orderId) throws ServiceException;
    boolean addItemToCart(Map<String, String> mapData) throws ServiceException;
    boolean removeItemFromCart(Map<String, String> mapData) throws ServiceException;
    int countItemsInCart(int cartOrderId) throws ServiceException;
    BigDecimal findCartTotalPrice(int cartOrderId) throws ServiceException;
}
