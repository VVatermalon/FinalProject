package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.CategoryDao;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDaoImpl extends CategoryDao {
    private static final String SQL_SELECT_ALL_CATEGORIES = """
        SELECT item_category_id, category_name FROM item_categories""";
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
    public boolean create(ItemCategory entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean update(ItemCategory entity) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
