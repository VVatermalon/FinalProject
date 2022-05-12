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

import java.util.*;

public class ItemService {
    private static final Logger logger = LogManager.getLogger();
    private static final ItemService INSTANCE = new ItemService();

    private ItemService() {
    }

    public static ItemService getInstance() {
        return INSTANCE;
    }

    public List<Item> findAllItems() throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items = itemDao.findAll();
            return setCategoriesAndSizesForItems(categoryDao, sizeDao, transaction, items);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Item> findAllByPage(int count, int offset) throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items = itemDao.findAllByPage(count, offset);
            return setCategoriesAndSizesForItems(categoryDao, sizeDao, transaction, items);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Item> findAllByPageSort(Item.ItemSortParameter sortParameter, SortOrder order, int count, int offset) throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items = itemDao.findAllByPageSort(sortParameter, order, count, offset);
            return setCategoriesAndSizesForItems(categoryDao, sizeDao, transaction, items);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Item> findAllByCategory(int id) throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items = itemDao.findAllByCategory(id);
            return setCategoriesAndSizesForItems(categoryDao, sizeDao, transaction, items);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Item> findAllByCategoryByPage(int id, int count, int offset) throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items = itemDao.findAllByCategoryByPage(id, count, offset);
            return setCategoriesAndSizesForItems(categoryDao, sizeDao, transaction, items);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Item> findAllByCategoryByPageSort(int id, Item.ItemSortParameter sortParameter, SortOrder order, int count, int offset) throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items = itemDao.findAllByCategoryByPageSort(id, sortParameter, order, count, offset);
            return setCategoriesAndSizesForItems(categoryDao, sizeDao, transaction, items);
        } catch (DaoException e) {
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

//    public boolean uploadCart(Map<Map.Entry<Integer, Integer>, Integer> cart, ArrayList<OrderComponent> uploadedCart) throws ServiceException {
//        boolean result = false;
//        for(Map.Entry<Integer, Integer> key: cart.keySet()) {
//            Item item = findItem(key.getKey());
//            ItemSize itemSize = findItemSize(key.getValue(), item);
//            if(itemSize == null) {
//                throw new ServiceException("Can't find item size");
//            }
//            int amountInStock = itemSize.getAmountInStock();
//            if(amountInStock == 0) {
//                cart.remove(key);
//                result = true;
//                continue;
//            }
//            if(amountInStock < cart.get(key)) {
//                cart.put(key, amountInStock);
//                result = true;
//            }
//            OrderComponent component = new OrderComponent(item, cart.get(key), itemSize);
//            uploadedCart.add(component);
//        }
//        return result;
//    }

//    public boolean addItemToCart(int itemId, int amount, String sizeParameter,
//                                 Map<Map.Entry<Integer, Integer>, Integer> cart) throws ServiceException {
//        int sizeId = sizeParameter == null ? 1 : Integer.parseInt(sizeParameter);
//        AbstractMap.SimpleEntry<Integer, Integer> key = new AbstractMap.SimpleEntry<>(itemId, sizeId);
//        Item item = findItem(itemId);
//        ItemSize itemSize = findItemSize(sizeId, item);
//        if(itemSize == null) {
//            return false;
//        }
//        int amountInStock = itemSize.getAmountInStock();
//        if(cart.containsKey(key)) {
//            int newAmount = cart.get(key) + amount;
//            if(amountInStock < newAmount || newAmount > 50) {
//                newAmount = Math.min(50, amountInStock);
//                cart.put(key, newAmount);
//                return false;
//            }
//            cart.put(key, newAmount);
//        } else {
//            if(amountInStock < amount || amount > 50) {
//                if(amountInStock > 0) {
//                    amount = Math.min(50, amountInStock);
//                    cart.put(key, amount);
//                }
//                return false;
//            }
//            cart.put(key, amount);
//        }
//        return true;
//    }

//    public boolean removeItemFromCart(int itemId, String sizeParameter,
//                                      Map<Map.Entry<Integer, Integer>, Integer> cart) {
//        int sizeId = sizeParameter == null ? 1 : Integer.parseInt(sizeParameter);
//        AbstractMap.SimpleEntry<Integer, Integer> key = new AbstractMap.SimpleEntry<>(itemId, sizeId);
//        return cart.remove(key) != null;
//    }
//
//    public boolean changeItemAmountInCart(int itemId, int amount, String sizeParameter,
//                                          Map<Map.Entry<Integer, Integer>, Integer> cart) throws ServiceException {
//        int sizeId = sizeParameter == null ? 1 : Integer.parseInt(sizeParameter);
//        AbstractMap.SimpleEntry<Integer, Integer> key = new AbstractMap.SimpleEntry<>(itemId, sizeId);
//        Item item = findItem(itemId);
//        ItemSize itemSize = findItemSize(sizeId, item);
//        if(itemSize == null) {
//            return false;
//        }
//        int amountInStock = itemSize.getAmountInStock();
//        if(cart.containsKey(key)) {
//            amount = Math.max(amount, 1);
//            if(amountInStock < amount || amount > 50) {
//                return false;
//            }
//            cart.put(key, amount);
//            return true;
//        }
//        return false;
//    }
//
//    private Item findItem(int itemId) throws ServiceException {
//        Optional<Item> itemOptional = findItemById(itemId);
//        if (itemOptional.isPresent()) {
//            return itemOptional.get();
//        } else {
//            throw new ServiceException("Can't find item by id: " + itemId);
//        }
//    }
//
//    private ItemSize findItemSize(Integer sizeId, Item item) {
//        Optional<ItemSize> sizeOpt = item.getSizes().stream()
//                .filter(s->s.getId()==sizeId)
//                .findFirst();
//        if(sizeOpt.isEmpty()) {
//            return null;
//        }
//        return sizeOpt.get();
//    }

    private List<Item> setCategoriesAndSizesForItems(CategoryDao categoryDao, SizeDao sizeDao, EntityTransaction transaction, List<Item> items) throws DaoException {
        for (Item item : items) {
            List<ItemCategory> categories = categoryDao.findAllCategoriesForItem(item.getId());
            item.setCategories(categories);
            List<ItemSize> sizes = sizeDao.findAllSizesForItem(item.getId());
            item.setSizes(sizes);
        }
        transaction.commit();
        return items;
    }
}
