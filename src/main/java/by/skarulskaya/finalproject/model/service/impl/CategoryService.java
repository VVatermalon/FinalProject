package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.impl.CategoryDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.ItemDaoImpl;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class CategoryService {
    private static final Logger logger = LogManager.getLogger();
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
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    public boolean create(String categoryName) throws ServiceException {
        CategoryDaoImpl categoryDao = new CategoryDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(categoryDao);
            ItemCategory category = new ItemCategory(categoryName);
            return categoryDao.create(category);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    public boolean update(ItemCategory category) throws ServiceException {
        CategoryDaoImpl categoryDao = new CategoryDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(categoryDao);
            return categoryDao.update(category);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    public boolean delete(int categoryId) throws ServiceException {
        CategoryDaoImpl categoryDao = new CategoryDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(categoryDao);
            return categoryDao.delete(categoryId);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    public boolean isCategoryNameUnique(String categoryName) throws ServiceException {
        CategoryDaoImpl categoryDao = new CategoryDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(categoryDao);
            return categoryDao.isCategoryNameUnique(categoryName);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    public boolean isCategoryNameUnique(String categoryName, int categoryId) throws ServiceException {
        CategoryDaoImpl categoryDao = new CategoryDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(categoryDao);
            return categoryDao.isCategoryNameUnique(categoryName, categoryId);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }
}
