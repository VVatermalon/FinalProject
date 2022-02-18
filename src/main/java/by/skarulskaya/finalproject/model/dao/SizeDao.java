package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.ItemSize;

import java.util.List;

public abstract class SizeDao extends AbstractDao<Integer, ItemSize> {
    public abstract List<ItemSize> findAllSizesForItem(int itemId) throws DaoException;
}
