package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.CategoryDao;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.ItemDao;
import by.skarulskaya.finalproject.model.dao.SizeDao;
import by.skarulskaya.finalproject.model.dao.impl.CategoryDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.ItemDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.SizeDaoImpl;
import by.skarulskaya.finalproject.model.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.*;

import static by.skarulskaya.finalproject.controller.Parameters.ORDER_STATUS_ANY;

public class ItemService {
    private static final Logger logger = LogManager.getLogger();
    private static final ItemService INSTANCE = new ItemService();

    private ItemService() {
    }

    public static ItemService getInstance() {
        return INSTANCE;
    }

    public List<Item> findAllByCategoryByPageSort(String categoryParameter, String sortParameter, String sortOrderParameter, int count, int offset) throws ServiceException {
        if((sortParameter == null && sortOrderParameter != null) || (sortParameter != null && sortOrderParameter == null)) {
            throw new ServiceException("Wrong item sort parameters: sort = " + sortParameter + ", sort order = " + sortOrderParameter);
        }
        ItemDao itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items;
            if (categoryParameter == null) {
                if(sortParameter == null) {
                    items = itemDao.findAllByPage(count, offset);
                }
                else {
                    Item.ItemSortParameter sort = Item.ItemSortParameter.valueOf(sortParameter);
                    SortOrder sortOrder = SortOrder.valueOf(sortOrderParameter);
                    items = itemDao.findAllByPageSort(sort, sortOrder, count, offset);
                }
            }
            else {
                int categoryId = Integer.parseInt(categoryParameter);
                if(sortParameter == null) {
                    items = itemDao.findAllByCategoryByPage(categoryId, count, offset);
                }
                else {
                    Item.ItemSortParameter sort = Item.ItemSortParameter.valueOf(sortParameter);
                    SortOrder sortOrder = SortOrder.valueOf(sortOrderParameter);
                    items = itemDao.findAllByCategoryByPageSort(categoryId, sort, sortOrder, count, offset);
                }
            }
            for (Item item : items) {
                List<ItemCategory> categories = categoryDao.findAllCategoriesForItem(item.getId());
                item.setCategories(categories);
                List<ItemSize> sizes = sizeDao.findAllSizesForItem(item.getId());
                item.setSizes(sizes);
            }
            transaction.commit();
            return items;
        } catch (DaoException | IllegalArgumentException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Item> findItemById(int id) throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            Optional<Item> itemOptional = itemDao.findEntityById(id);
            if (itemOptional.isPresent()) {
                Item item = itemOptional.get();
                List<ItemCategory> categories = categoryDao.findAllCategoriesForItem(item.getId());
                item.setCategories(categories);
                List<ItemSize> sizes = sizeDao.findAllSizesForItem(item.getId());
                item.setSizes(sizes);
            }
            transaction.commit();
            return itemOptional;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean updatePopularity(int id) throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(itemDao);
            long popularity = System.currentTimeMillis();
            return itemDao.updatePopularity(id, popularity);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
