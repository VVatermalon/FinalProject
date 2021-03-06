package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.ItemCategory;

import java.util.List;

public abstract class CategoryDao extends AbstractDao<Integer, ItemCategory> {
    public abstract List<ItemCategory> findAllCategoriesForItem(int itemId) throws DaoException;

    public abstract boolean delete(int categoryId, int itemId) throws DaoException;

    public abstract boolean createItemCategory(int categoryId, int itemId) throws DaoException;

    public abstract boolean isCategoryNameUnique(String name) throws DaoException;

    public abstract boolean isCategoryNameUnique(String name, int id) throws DaoException;
}
