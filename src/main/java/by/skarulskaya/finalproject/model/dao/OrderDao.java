package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.Order;

import java.math.BigDecimal;
import java.util.List;

public abstract class OrderDao extends AbstractDao<Integer, Order> {
    public abstract int createCartOrder(int customerId) throws DaoException;
    public abstract int findCartOrderId(int customerId) throws DaoException;
    public abstract List<Order> findAllRegisteredOrders(int customerId) throws DaoException;
    public abstract boolean registerOrder(int orderId, int addressId, BigDecimal cartTotalPrice) throws DaoException;
}
