package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;

import java.util.List;

public abstract class ItemDao extends AbstractDao<Integer, Item> {
    public abstract List<Item> findAllByName(String name) throws DaoException;
    public abstract List<Item> findAllByCategory(Integer id) throws DaoException;
}
