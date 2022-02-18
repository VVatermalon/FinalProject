package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.CategoryDao;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.SizeDao;
import by.skarulskaya.finalproject.model.dao.impl.CategoryDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.ItemDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.SizeDaoImpl;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.entity.ItemSize;
import by.skarulskaya.finalproject.model.entity.SortOrder;

import java.util.List;
import java.util.Optional;

public class ItemService {
    private static final ItemService INSTANCE = new ItemService();

    private ItemService() {
    }

    public static ItemService getInstance() {
        return INSTANCE;
    }

    public List<Item> findAllItems() throws ServiceException {
        ItemDaoImpl itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items = itemDao.findAll();
            for (Item item : items) {
                List<ItemCategory> categories = categoryDao.findAllCategoriesForItem(item.getId());
                item.setCategories(categories);
                List<ItemSize> sizes = sizeDao.findAllSizesForItem(item.getId());
                item.setSizes(sizes);
            }
            transaction.commit();
            return items;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Item> findAllByPage(int count, int offset) throws ServiceException {
        ItemDaoImpl itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items = itemDao.findAllByPage(count, offset);
            for (Item item : items) {
                List<ItemCategory> categories = categoryDao.findAllCategoriesForItem(item.getId());
                item.setCategories(categories);
                List<ItemSize> sizes = sizeDao.findAllSizesForItem(item.getId());
                item.setSizes(sizes);
            }
            transaction.commit();
            return items;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Item> findAllByPageSort(Item.ItemSortParameter sortParameter, SortOrder order, int count, int offset) throws ServiceException {
        ItemDaoImpl itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items = itemDao.findAllByPageSort(sortParameter, order, count, offset);
            for (Item item : items) {
                List<ItemCategory> categories = categoryDao.findAllCategoriesForItem(item.getId());
                item.setCategories(categories);
                List<ItemSize> sizes = sizeDao.findAllSizesForItem(item.getId());
                item.setSizes(sizes);
            }
            transaction.commit();
            return items;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Item> findAllByCategory(int id) throws ServiceException {
        ItemDaoImpl itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items = itemDao.findAllByCategory(id);
            for (Item item : items) {
                List<ItemCategory> categories = categoryDao.findAllCategoriesForItem(item.getId());
                item.setCategories(categories);
                List<ItemSize> sizes = sizeDao.findAllSizesForItem(item.getId());
                item.setSizes(sizes);
            }
            transaction.commit();
            return items;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Item> findAllByCategoryByPage(int id, int count, int offset) throws ServiceException {
        ItemDaoImpl itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items = itemDao.findAllByCategoryByPage(id, count, offset);
            for (Item item : items) {
                List<ItemCategory> categories = categoryDao.findAllCategoriesForItem(item.getId());
                item.setCategories(categories);
                List<ItemSize> sizes = sizeDao.findAllSizesForItem(item.getId());
                item.setSizes(sizes);
            }
            transaction.commit();
            return items;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Item> findAllByCategoryByPageSort(int id, Item.ItemSortParameter sortParameter, SortOrder order, int count, int offset) throws ServiceException {
        ItemDaoImpl itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items = itemDao.findAllByCategoryByPageSort(id, sortParameter, order, count, offset);
            for (Item item : items) {
                List<ItemCategory> categories = categoryDao.findAllCategoriesForItem(item.getId());
                item.setCategories(categories);
                List<ItemSize> sizes = sizeDao.findAllSizesForItem(item.getId());
                item.setSizes(sizes);
            }
            transaction.commit();
            return items;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Item> findItemById(int id) throws ServiceException {
        ItemDaoImpl itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            Optional<Item> itemOptional = itemDao.findEntityById(id);
            if(itemOptional.isPresent()) {
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
}
