package by.skarulskaya.finalproject.model.service;

import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    List<Order> findAllCustomerRegisteredOrders(int customerId) throws ServiceException;
    List<Order> findAllRegisteredOrders() throws ServiceException;
    List<Order> findAllRegisteredOrdersByPage(int count, int offset) throws ServiceException;
    List<Order> findAllRegisteredOrdersByDateByStatusByPage(String date, String status, int count, int offset) throws ServiceException;
    int createCart(int customerId) throws ServiceException;
    int findCartOrderId(int customerId) throws ServiceException;
    boolean registerOrder(Customer customer, int cartOrderId, int addressId, BigDecimal cartTotalPrice) throws ServiceException;
    boolean confirmOrder(int orderId) throws ServiceException;
    boolean cancelOrder(int orderId) throws ServiceException;
}
