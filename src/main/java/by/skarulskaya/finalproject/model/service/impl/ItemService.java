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
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static by.skarulskaya.finalproject.controller.Parameters.*;

public class ItemService {
    private static final Logger logger = LogManager.getLogger();
    private static final String DEFAULT_IMAGE_PATH = "defaultItem.png";
    private static final ItemService INSTANCE = new ItemService();

    private ItemService() {
    }

    public static ItemService getInstance() {
        return INSTANCE;
    }

    public List<Item> findAllByCategoryByPageSort(String categoryParameter, String sortParameter, String sortOrderParameter, int count, int offset) throws ServiceException {
        if ((sortParameter == null && sortOrderParameter != null) || (sortParameter != null && sortOrderParameter == null)) {
            throw new ServiceException("Wrong item sort parameters: sort = " + sortParameter + ", sort order = " + sortOrderParameter);
        }
        ItemDao itemDao = new ItemDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, categoryDao, sizeDao);
            List<Item> items;
            if (categoryParameter == null) {
                if (sortParameter == null) {
                    items = itemDao.findAllByPage(count, offset);
                } else {
                    Item.ItemSortParameter sort = Item.ItemSortParameter.valueOf(sortParameter);
                    SortOrder sortOrder = SortOrder.valueOf(sortOrderParameter);
                    items = itemDao.findAllByPageSort(sort, sortOrder, count, offset);
                }
            } else {
                int categoryId = Integer.parseInt(categoryParameter);
                if (sortParameter == null) {
                    items = itemDao.findAllByCategoryByPage(categoryId, count, offset);
                } else {
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

    public boolean updateOrCreate(HashMap<String, String> map, List<Integer> sizesId,
                                  List<String> amountsInStock, List<Integer> categoriesId) throws ServiceException {
        if (!BaseValidatorImpl.INSTANCE.validateUpdateItem(map, sizesId, amountsInStock, categoriesId)) {
            return false;
        }
        String itemId = map.get(ITEM_ID);
        String name = map.get(ITEM_NAME);
        String priceParameter = map.get(ITEM_PRICE);
        String description = map.get(ITEM_DESCRIPTION);
        ArrayList<ItemCategory> categories = new ArrayList<>(10);
        for (int categoryId : categoriesId) {
            ItemCategory category = new ItemCategory(categoryId);
            categories.add(category);
        }
        ArrayList<ItemSize> sizes = new ArrayList<>(10);
        for (int i = 0; i < sizesId.size(); i++) {
            ItemSize size = new ItemSize(sizesId.get(i), Integer.parseInt(amountsInStock.get(i)));
            sizes.add(size);
        }
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(priceParameter));
        Item item = new Item(name, categories, sizes, price, 0, System.currentTimeMillis(), description, DEFAULT_IMAGE_PATH);
        if (itemId == null) {
            return createItem(item, map);
        } else {
            return updateItem(item, map);
        }
    }

    public boolean updateItemImagePath(int id, String path) throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(itemDao);
            return itemDao.updateImagePath(id, path);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean updatePopularity(int id) throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(itemDao);
            long popularity = System.currentTimeMillis();
            return itemDao.updatePopularity(id, popularity);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean delete(int id) throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(itemDao);
            return itemDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private boolean createItem(Item item, HashMap<String, String> dataMap) throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, sizeDao, categoryDao);
            if(!itemDao.checkItemNameUnique(item.getName())) {
                dataMap.put(ITEM_NAME, NOT_UNIQUE_ITEM_NAME);
                return false;
            }
            if (!itemDao.create(item)) {
                transaction.rollback();
                return false;
            }
            int id = item.getId();
            dataMap.put(ITEM_ID, Integer.toString(id));
            for (ItemSize newSize : item.getSizes()) {
                if (!sizeDao.createItemSize(newSize, id)) {
                    transaction.rollback();
                    return false;
                }
            }
            for (ItemCategory newCategory : item.getCategories()) {
                if (!categoryDao.createItemCategory(newCategory.getId(), id)) {
                    transaction.rollback();
                    return false;
                }
            }
            transaction.commit();
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private boolean updateItem(Item item, HashMap<String, String> dataMap) throws ServiceException {
        ItemDao itemDao = new ItemDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(itemDao, sizeDao, categoryDao);
            int id = Integer.parseInt(dataMap.get(ITEM_ID));
            if(!itemDao.checkItemNameUnique(item.getName(), id)) {
                dataMap.put(ITEM_NAME, NOT_UNIQUE_ITEM_NAME);
                return false;
            }
            ArrayList<ItemSize> itemOldSizes;
            ArrayList<ItemCategory> itemOldCategories;
            item.setId(id);
            if (!itemDao.update(item)) {
                transaction.rollback();
                return false;
            }
            itemOldSizes = (ArrayList<ItemSize>) sizeDao.findAllSizesForItem(id);
            itemOldCategories = (ArrayList<ItemCategory>) categoryDao.findAllCategoriesForItem(id);
            for (ItemSize newSize : item.getSizes()) {
                if (!itemOldSizes.removeIf(oldSize -> oldSize.getId() == newSize.getId()
                        && oldSize.getAmountInStock() == newSize.getAmountInStock())) {
                    if (itemOldSizes.removeIf(oldSize -> oldSize.getId() == newSize.getId())) {
                        if (!sizeDao.updateItemSize(newSize, id)) {
                            transaction.rollback();
                            return false;
                        }
                    } else {
                        if (!sizeDao.createItemSize(newSize, id)) {
                            transaction.rollback();
                            return false;
                        }
                    }
                }
            }
            for (ItemSize oldSize : itemOldSizes) {
                if (!sizeDao.delete(oldSize.getId(), id)) {
                    transaction.rollback();
                    return false;
                }
            }
            for (ItemCategory newCategory : item.getCategories()) {
                if (!itemOldCategories.removeIf(oldCategory -> oldCategory.getId() == newCategory.getId())) {
                    if (!categoryDao.createItemCategory(newCategory.getId(), id)) {
                        transaction.rollback();
                        return false;
                    }
                }
            }
            for (ItemCategory oldCategory : itemOldCategories) {
                if (!categoryDao.delete(oldCategory.getId(), id)) {
                    transaction.rollback();
                    return false;
                }
            }
            transaction.commit();
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
