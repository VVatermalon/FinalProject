package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.OrderComponent;

import java.util.HashMap;

public abstract class OrderComponentDao extends AbstractDao<OrderComponent.OrderComponentKey, OrderComponent> {
    public abstract boolean createByKey(OrderComponent.OrderComponentKey key, int amount) throws DaoException;
    public abstract HashMap<OrderComponent.OrderComponentKey, Integer> findAllCartComponents(int cartOrderId) throws DaoException;
    public abstract boolean changeItemAmountInCart(OrderComponent.OrderComponentKey key, int amount) throws DaoException;
    public abstract int findItemAmountInCart(OrderComponent.OrderComponentKey key) throws DaoException;
    public abstract int countItemsInCart(int cartOrderId) throws DaoException;
}
