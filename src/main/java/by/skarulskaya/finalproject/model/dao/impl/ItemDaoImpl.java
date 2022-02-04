package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.ItemDao;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;
import by.skarulskaya.finalproject.model.mapper.impl.ItemMapper;
import by.skarulskaya.finalproject.model.mapper.impl.UserMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDaoImpl extends ItemDao {
    private static final String SQL_SELECT_ALL_ITEMS = """
        SELECT I.item_id, I.item_name, I.category_id, C.category_name, I.price, I.amount_in_stock, I.popularity, 
        I.image_path, I.description FROM items I JOIN item_categories C on I.category_id = C.item_category_id""";
    private static final String SQL_FIND_ITEM_BY_ID = """
        SELECT I.item_id, I.item_name, I.category_id, C.category_name, I.price, I.amount_in_stock, I.popularity, 
        I.image_path, I.description FROM items I JOIN item_categories C on I.category_id = C.item_category_id
        WHERE I.item_id = ?""";
    private static final String SQL_FIND_ITEMS_BY_CATEGORY = """
        SELECT I.item_id, I.item_name, I.category_id, C.category_name, I.price, I.amount_in_stock, I.popularity, 
        I.image_path, I.description FROM items I JOIN item_categories C on I.category_id = C.item_category_id
        WHERE I.category_id = ?""";
    private static final EntityMapper<Item> mapper = new ItemMapper();

    @Override
    public List<Item> findAll() throws DaoException {
        List<Item> itemList = new ArrayList<>();
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_ITEMS)) {
            while(resultSet.next()) {
                Item item = mapper.map(resultSet);
                itemList.add(item);
            }
            return itemList;
         } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Item> findEntityById(Integer id) throws DaoException {
        ResultSet resultSet = null;
        try(PreparedStatement statement = connection.prepareStatement(SQL_FIND_ITEM_BY_ID)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Item item = mapper.map(resultSet);
                return Optional.of(item);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            close(resultSet);
        }
    }
    @Override
    public List<Item> findAllByCategory(Integer id) throws DaoException {
        ResultSet resultSet = null;
        List<Item> itemList = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(SQL_FIND_ITEMS_BY_CATEGORY)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Item item = mapper.map(resultSet);
                itemList.add(item);
            }
            return itemList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            close(resultSet);
        }
    }

    @Override
    public boolean delete(Item entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean create(Item entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean update(Item entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Item> findAllByName(String name) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
