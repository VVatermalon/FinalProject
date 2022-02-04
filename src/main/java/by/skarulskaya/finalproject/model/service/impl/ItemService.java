package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.impl.ItemDaoImpl;
import by.skarulskaya.finalproject.model.entity.Item;

import java.util.List;
import java.util.Optional;

public class ItemService {
    private static final ItemService INSTANCE = new ItemService();
    private ItemService(){}
    public static ItemService getInstance() {
        return INSTANCE;
    }

    public List<Item> findAllItems() throws ServiceException {
        ItemDaoImpl itemDao = new ItemDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(itemDao);
            return itemDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Item> findItemById(int id) throws ServiceException {
        ItemDaoImpl itemDao = new ItemDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(itemDao);
            return itemDao.findEntityById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Item> findAllByCategory(int id) throws ServiceException {
        ItemDaoImpl itemDao = new ItemDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(itemDao);
            return itemDao.findAllByCategory(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
