package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.Order;

import java.math.BigDecimal;
import java.util.List;

public abstract class OrderDao extends AbstractDao<Integer, Order> {
    public abstract int createCartOrder(int customerId) throws DaoException;
    public abstract int findCartOrderId(int customerId) throws DaoException;

    public abstract List<Order> findAllByPage(int count, int offset) throws DaoException;

    public abstract List<Order> findAllCustomerRegisteredOrders(int customerId) throws DaoException;
    public abstract boolean registerOrder(int orderId, int addressId, BigDecimal cartTotalPrice) throws DaoException;
    public abstract String findOrderCustomerEmail(int orderId) throws DaoException;
    public abstract boolean addMoneyToOrderCustomerAccount(int orderId) throws DaoException;
    public abstract boolean changeOrderStatus(int orderId, Order.OrderStatus newStatus) throws DaoException;
}
