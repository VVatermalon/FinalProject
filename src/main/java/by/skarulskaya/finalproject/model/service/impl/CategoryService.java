package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.impl.CategoryDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.ItemDaoImpl;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;

import java.util.List;

public class CategoryService {
    private static final CategoryService INSTANCE = new CategoryService();
    private CategoryService(){}
    public static CategoryService getInstance() {
        return INSTANCE;
    }

    public List<ItemCategory> findAllCategories() throws ServiceException {
        CategoryDaoImpl categoryDao = new CategoryDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(categoryDao);
            return categoryDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
