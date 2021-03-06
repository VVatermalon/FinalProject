package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.CategoryDao;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.entity.ItemSize;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDaoImpl extends CategoryDao {
    private static final String SQL_SELECT_ALL_CATEGORIES = """
        SELECT item_category_id, category_name FROM item_categories""";
    private static final String SQL_SELECT_ALL_CATEGORIES_FOR_ITEM = """
            SELECT C.item_category_id, C.category_name FROM items_item_categories IC 
            JOIN item_categories C on IC.item_category_id = C.item_category_id
            WHERE IC.item_id = ?""";
    private static final String SQL_CREATE_ITEM_CATEGORY = """
            INSERT INTO items_item_categories(item_category_id, item_id) VALUES(?,?)""";
    private static final String SQL_DELETE_ITEM_CATEGORY = """
            DELETE FROM items_item_categories WHERE item_category_id = ? AND item_id = ?""";
    private static final String SQL_CREATE_CATEGORY = """
            INSERT INTO item_categories(category_name) VALUES(?)""";
    private static final String SQL_IS_CATEGORY_NAME_UNIQUE = """
            SELECT * from item_categories WHERE category_name = ?""";
    private static final String SQL_IS_CATEGORY_NAME_UNIQUE_EXCEPT_CATEGORY_WITH_ID = """
            SELECT * from item_categories WHERE category_name = ? AND item_category_id <> ?""";
    private static final String SQL_UPDATE_CATEGORY = """
            UPDATE item_categories SET category_name = ? WHERE item_category_id = ?""";
    private static final String SQL_DELETE_CATEGORY = """
            DELETE FROM item_categories WHERE item_category_id = ?""";
    @Override
    public List<ItemCategory> findAll() throws DaoException {
        List<ItemCategory> categoryList = new ArrayList<>();
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_CATEGORIES)) {
            while(resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                ItemCategory category = new ItemCategory(id, name);
                categoryList.add(category);
            }
            return categoryList;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    public List<ItemCategory> findAllCategoriesForItem(int itemId) throws DaoException {
        List<ItemCategory> categoryList = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_CATEGORIES_FOR_ITEM)) {
            statement.setInt(1, itemId);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    ItemCategory category = new ItemCategory(id, name);
                    categoryList.add(category);
                }
                return categoryList;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<ItemCategory> findEntityById(Integer id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(ItemCategory entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_CATEGORY)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(int categoryId, int itemId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ITEM_CATEGORY)) {
            statement.setInt(1, categoryId);
            statement.setInt(2, itemId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean create(ItemCategory entity) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_CREATE_CATEGORY)) {
            statement.setString(1, entity.getCategoryName());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean createItemCategory(int categoryId, int itemId) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_CREATE_ITEM_CATEGORY)) {
            statement.setInt(1, categoryId);
            statement.setInt(2, itemId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(ItemCategory entity) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_CATEGORY)) {
            statement.setString(1, entity.getCategoryName());
            statement.setInt(2, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isCategoryNameUnique(String name) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_IS_CATEGORY_NAME_UNIQUE)) {
            statement.setString(1, name);
            return !statement.executeQuery().next();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isCategoryNameUnique(String name, int id) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_IS_CATEGORY_NAME_UNIQUE_EXCEPT_CATEGORY_WITH_ID)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            return !statement.executeQuery().next();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }
}
