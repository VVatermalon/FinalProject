package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.entity.SortOrder;

import java.util.List;

public abstract class ItemDao extends AbstractDao<Integer, Item> {
    public abstract List<Item> findAllByName(String name) throws DaoException;

    public abstract List<Item> findAllByPageSort(Item.ItemSortParameter sortParameter, SortOrder order, int count, int offset) throws DaoException;

    public abstract List<Item> findAllByPage(int count, int offset) throws DaoException;
    public abstract List<Item> findAllByCategoryByPage(int id, int count, int offset) throws DaoException;

    public abstract List<Item> findAllByCategoryByPageSort(Integer id, Item.ItemSortParameter sortParameter, SortOrder order, int count, int offset) throws DaoException;

    public abstract boolean updatePopularity(int id, long popularity) throws DaoException;
}
