package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.ItemSize;

import java.util.List;

public abstract class SizeDao extends AbstractDao<Integer, ItemSize> {
    public abstract List<ItemSize> findAllSizesForItem(int itemId) throws DaoException;

    public abstract boolean delete(int sizeId, int itemId) throws DaoException;

    public abstract boolean createItemSize(ItemSize entity, int itemId) throws DaoException;

    public abstract boolean updateItemSize(ItemSize entity, int itemId) throws DaoException;

    public abstract boolean changeAmountInStockForAllOrderItems(int orderId) throws DaoException;

    public abstract boolean changeBackAmountInStockForAllOrderItems(int orderId) throws DaoException;
}
