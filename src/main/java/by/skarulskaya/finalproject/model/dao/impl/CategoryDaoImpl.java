package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.CategoryDao;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.entity.ItemSize;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(int categoryId, int itemId) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ITEM_CATEGORY)) {
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
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }
}
