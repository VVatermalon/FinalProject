package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.ItemDao;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.SortOrder;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;
import by.skarulskaya.finalproject.model.mapper.impl.ItemMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDaoImpl extends ItemDao {
    private static final String SQL_SELECT_ALL_ITEMS = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, I.image_path, I.description
        FROM items I JOIN items_item_sizes S ON I.item_id = S.item_id GROUP BY I.item_id""";
    private static final String SQL_SELECT_ITEMS_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, I.image_path, I.description 
        FROM items I JOIN items_item_sizes S ON I.item_id = S.item_id GROUP BY I.item_id
        ORDER BY sum(S.amount_in_stock) = 0
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEMS_SORT_BY_PRICE_ASC_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, I.image_path, I.description
        FROM items I JOIN items_item_sizes S ON I.item_id = S.item_id GROUP BY I.item_id
        ORDER BY sum(S.amount_in_stock) = 0, I.price
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEMS_SORT_BY_PRICE_DESC_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, I.image_path, I.description
        FROM items I JOIN items_item_sizes S ON I.item_id = S.item_id GROUP BY I.item_id
        ORDER BY sum(S.amount_in_stock) = 0 ASC, I.price DESC
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEMS_SORT_BY_NAME_ASC_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, I.image_path, I.description
        FROM items I JOIN items_item_sizes S ON I.item_id = S.item_id GROUP BY I.item_id
        ORDER BY sum(S.amount_in_stock) = 0, I.item_name
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEMS_SORT_BY_NAME_DESC_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, I.image_path, I.description
        FROM items I JOIN items_item_sizes S ON I.item_id = S.item_id GROUP BY I.item_id
        ORDER BY sum(S.amount_in_stock) = 0 ASC, I.item_name DESC
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEMS_SORT_BY_POPULARITY_ASC_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, I.image_path, I.description
        FROM items I JOIN items_item_sizes S ON I.item_id = S.item_id GROUP BY I.item_id
        ORDER BY sum(S.amount_in_stock) = 0, I.popularity
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEMS_SORT_BY_POPULARITY_DESC_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, I.image_path, I.description
        FROM items I JOIN items_item_sizes S ON I.item_id = S.item_id GROUP BY I.item_id
        ORDER BY sum(S.amount_in_stock) = 0 ASC, I.popularity DESC
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEM_BY_ID = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, I.image_path, I.description
        FROM items I JOIN items_item_sizes S ON I.item_id = S.item_id 
        WHERE I.item_id = ? GROUP BY I.item_id""";
    private static final String SQL_SELECT_ITEMS_BY_CATEGORY = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, 
        I.image_path, I.description FROM items I
        JOIN items_item_categories IC ON I.item_id = IC.item_id
        JOIN items_item_sizes S ON I.item_id = S.item_id
        WHERE IC.item_category_id = ? GROUP BY I.item_id""";
    private static final String SQL_SELECT_ITEMS_BY_CATEGORY_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, 
        I.image_path, I.description FROM items I
        JOIN items_item_categories IC ON I.item_id = IC.item_id
        JOIN items_item_sizes S ON I.item_id = S.item_id WHERE IC.item_category_id = ? GROUP BY I.item_id
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEMS_BY_CATEGORY_SORT_BY_PRICE_ASC_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, 
        I.image_path, I.description FROM items I
        JOIN items_item_categories IC on I.item_id = IC.item_id
        JOIN items_item_sizes S on I.item_id = S.item_id
        WHERE IC.item_category_id = ? group by I.item_id ORDER BY I.price
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEMS_BY_CATEGORY_SORT_BY_PRICE_DESC_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, 
        I.image_path, I.description FROM items I
        JOIN items_item_categories IC on I.item_id = IC.item_id
        JOIN items_item_sizes S on I.item_id = S.item_id
        WHERE IC.item_category_id = ? group by I.item_id ORDER BY I.price DESC
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEMS_BY_CATEGORY_SORT_BY_NAME_ASC_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, 
        I.image_path, I.description FROM items I
        JOIN items_item_categories IC on I.item_id = IC.item_id
        JOIN items_item_sizes S on I.item_id = S.item_id
        WHERE IC.item_category_id = ? group by I.item_id ORDER BY I.item_name
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEMS_BY_CATEGORY_SORT_BY_NAME_DESC_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, 
        I.image_path, I.description FROM items I
        JOIN items_item_categories IC on I.item_id = IC.item_id
        JOIN items_item_sizes S on I.item_id = S.item_id
        WHERE IC.item_category_id = ? group by I.item_id ORDER BY I.item_name DESC
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEMS_BY_CATEGORY_SORT_BY_POPULARITY_ASC_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, 
        I.image_path, I.description FROM items I
        JOIN items_item_categories IC on I.item_id = IC.item_id
        JOIN items_item_sizes S on I.item_id = S.item_id
        WHERE IC.item_category_id = ? group by I.item_id ORDER BY I.popularity
        LIMIT ? OFFSET ?""";
    private static final String SQL_SELECT_ITEMS_BY_CATEGORY_SORT_BY_POPULARITY_DESC_LIMIT = """
        SELECT I.item_id, I.item_name, I.price, sum(S.amount_in_stock) amount_in_stock, I.popularity, 
        I.image_path, I.description FROM items I
        JOIN items_item_categories IC on I.item_id = IC.item_id
        JOIN items_item_sizes S on I.item_id = S.item_id
        WHERE IC.item_category_id = ? group by I.item_id ORDER BY I.popularity DESC
        LIMIT ? OFFSET ?""";
    private static final String SQL_UPDATE_POPULARITY = """
        UPDATE items SET popularity = (popularity + ?) / 2 WHERE item_id = ?""";
    private static final String SQL_CREATE = """
        INSERT INTO items(item_name, price, popularity, image_path, description) VALUES (?,?,?,?,?)""";
    private static final String SQL_UPDATE = """
        UPDATE items SET item_name = ?, price = ?, description = ? WHERE item_id = ?""";
    private static final String SQL_UPDATE_IMAGE_PATH = """
        UPDATE items SET image_path = ? WHERE item_id = ?""";
    private static final String SQL_DELETE = """
        DELETE FROM items WHERE item_id = ?""";
    private static final String SQL_CHECK_NAME_UNIQUE = """
        SELECT * FROM items WHERE item_name = ?""";
    private static final String SQL_CHECK_NAME_UNIQUE_EXCEPT_ITEM_WITH_ID = """
        SELECT * FROM items WHERE item_name = ? AND item_id <> ?""";
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
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Item> findAllByPage(int count, int offset) throws DaoException {
        List<Item> itemList = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ITEMS_LIMIT)) {
            statement.setInt(1, count);
            statement.setInt(2, offset);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = mapper.map(resultSet);
                    itemList.add(item);
                }
            }
            return itemList;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Item> findAllByPageSort(Item.ItemSortParameter sortParameter, SortOrder order, int count, int offset) throws DaoException {
        List<Item> itemList = new ArrayList<>();
        String sql = null;
        if(order == SortOrder.ASC) {
            switch (sortParameter) {
                case PRICE -> sql = SQL_SELECT_ITEMS_SORT_BY_PRICE_ASC_LIMIT;
                case ITEM_NAME -> sql = SQL_SELECT_ITEMS_SORT_BY_NAME_ASC_LIMIT;
                case POPULARITY -> sql = SQL_SELECT_ITEMS_SORT_BY_POPULARITY_ASC_LIMIT;
            }
        }
        else {
            switch (sortParameter) {
                case PRICE -> sql = SQL_SELECT_ITEMS_SORT_BY_PRICE_DESC_LIMIT;
                case ITEM_NAME -> sql = SQL_SELECT_ITEMS_SORT_BY_NAME_DESC_LIMIT;
                case POPULARITY -> sql = SQL_SELECT_ITEMS_SORT_BY_POPULARITY_DESC_LIMIT;
            }
        }
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, count);
            statement.setInt(2, offset);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = mapper.map(resultSet);
                    itemList.add(item);
                }
            }
            return itemList;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Item> findEntityById(Integer id) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ITEM_BY_ID)) {
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Item item = mapper.map(resultSet);
                    return Optional.of(item);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Item> findAllByCategoryByPage(int id, int count, int offset) throws DaoException {
        List<Item> itemList = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ITEMS_BY_CATEGORY_LIMIT)) {
            statement.setInt(1, id);
            statement.setInt(2, count);
            statement.setInt(3, offset);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = mapper.map(resultSet);
                    itemList.add(item);
                }
            }
            return itemList;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Item> findAllByCategoryByPageSort(Integer id, Item.ItemSortParameter sortParameter, SortOrder order, int count, int offset) throws DaoException {
        List<Item> itemList = new ArrayList<>();
        String sql = null;
        if(order == SortOrder.ASC) {
            switch (sortParameter) {
                case PRICE -> sql = SQL_SELECT_ITEMS_BY_CATEGORY_SORT_BY_PRICE_ASC_LIMIT;
                case ITEM_NAME -> sql = SQL_SELECT_ITEMS_BY_CATEGORY_SORT_BY_NAME_ASC_LIMIT;
                case POPULARITY -> sql = SQL_SELECT_ITEMS_BY_CATEGORY_SORT_BY_POPULARITY_ASC_LIMIT;
            }
        }
        else {
            switch (sortParameter) {
                case PRICE -> sql = SQL_SELECT_ITEMS_BY_CATEGORY_SORT_BY_PRICE_DESC_LIMIT;
                case ITEM_NAME -> sql = SQL_SELECT_ITEMS_BY_CATEGORY_SORT_BY_NAME_DESC_LIMIT;
                case POPULARITY -> sql = SQL_SELECT_ITEMS_BY_CATEGORY_SORT_BY_POPULARITY_DESC_LIMIT;
            }
        }
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, count);
            statement.setInt(3, offset);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = mapper.map(resultSet);
                    itemList.add(item);
                }
            }
            return itemList;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean updateImagePath(Integer id, String path) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_IMAGE_PATH)) {
            statement.setString(1, path);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean updatePopularity(int id, long popularity) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_POPULARITY)) {
            statement.setLong(1, popularity);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Item entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean create(Item entity) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setBigDecimal(2, entity.getPrice());
            statement.setLong(3, entity.getPopularity());
            statement.setString(4, entity.getImagePath());
            statement.setString(5, entity.getDescription());
            statement.executeUpdate();
            int generatedId;
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    connection.rollback();
                    throw new DaoException("ItemDao exception: no generated key in create method");
                }
                generatedId = keys.getInt(1);
                entity.setId(generatedId);
                return true;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Item entity) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, entity.getName());
            statement.setBigDecimal(2, entity.getPrice());
            statement.setString(3, entity.getDescription());
            statement.setInt(4, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Item> findAllByName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean checkItemNameUnique(String name) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_CHECK_NAME_UNIQUE)) {
            statement.setString(1, name);
            return !statement.executeQuery().next();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean checkItemNameUnique(String name, int itemId) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_CHECK_NAME_UNIQUE_EXCEPT_ITEM_WITH_ID)) {
            statement.setString(1, name);
            statement.setInt(2, itemId);
            return !statement.executeQuery().next();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }
}
