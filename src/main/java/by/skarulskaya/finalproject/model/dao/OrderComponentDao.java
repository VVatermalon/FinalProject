package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.OrderComponent;

import java.math.BigDecimal;
import java.util.HashMap;

public abstract class OrderComponentDao extends AbstractDao<OrderComponent.OrderComponentKey, OrderComponent> {
    public abstract boolean createByKey(OrderComponent.OrderComponentKey key, int amount) throws DaoException;
    public abstract HashMap<OrderComponent.OrderComponentKey, Integer> findAllOrderComponents(int orderId) throws DaoException;
    public abstract boolean changeItemAmountInCart(OrderComponent.OrderComponentKey key, int amount) throws DaoException;
    public abstract int findItemAmountInCart(OrderComponent.OrderComponentKey key) throws DaoException;
    public abstract int countItemsInCart(int cartOrderId) throws DaoException;
    public abstract BigDecimal findCartTotalPrice(int cartOrderId) throws DaoException;
    public abstract boolean findNotEnoughInStockItemsInOrder(int cartOrderId) throws DaoException;
}
