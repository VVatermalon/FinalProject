package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.SizeDao;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.entity.ItemSize;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SizeDaoImpl extends SizeDao {
    private static final String SQL_SELECT_ALL_SIZES_FOR_ITEM = """
            SELECT S.item_size_id, S.item_size_name, ItemS.amount_in_stock FROM items_item_sizes ItemS 
            JOIN item_sizes S on ItemS.item_size_id = S.item_size_id
            WHERE ItemS.item_id = ?""";
    private static final String SQL_SELECT_ALL = """
            SELECT item_size_id, item_size_name FROM item_sizes""";
    private static final String SQL_SELECT_ALL_BY_ORDER_ID = """
            SELECT orders.item_id, orders.item_size_id, orders.amount, item_sizes.amount_in_stock
            FROM orders_items orders JOIN items_item_sizes item_sizes
            ON (orders.item_id = item_sizes.item_id AND orders.item_size_id = item_sizes.item_size_id)
            WHERE order_id = ?""";
    private static final String SQL_SELECT_SIZE_BY_ID = """
            SELECT item_size_id, item_size_name FROM item_sizes where item_size_id = ?""";
    private static final String SQL_UPDATE_AMOUNT_IN_STOCK = """
            UPDATE items_item_sizes SET amount_in_stock = ? WHERE item_id = ? AND item_size_id = ?""";
    private static final String SQL_CREATE_ITEM_SIZE = """
            INSERT INTO items_item_sizes(item_size_id, item_id, amount_in_stock) VALUES(?,?,?)""";
    private static final String SQL_UPDATE_ITEM_SIZE = """
            UPDATE items_item_sizes SET amount_in_stock = ? WHERE item_size_id = ? AND item_id = ?""";
    private static final String SQL_DELETE_ITEM_SIZE = """
            DELETE FROM items_item_sizes WHERE item_size_id = ? AND item_id = ?""";
    private static final String SQL_CREATE_SIZE = """
            INSERT INTO item_sizes(item_size_name) VALUES(?)""";
    private static final String SQL_IS_SIZE_NAME_UNIQUE = """
            SELECT * from item_sizes WHERE item_size_name = ?""";
    private static final String SQL_IS_SIZE_NAME_UNIQUE_EXCEPT_SIZE_WITH_ID = """
            SELECT * from item_sizes WHERE item_size_name = ? AND item_size_id <> ?""";
    private static final String SQL_UPDATE_SIZE = """
            UPDATE item_sizes SET item_size_name = ? WHERE item_size_id = ?""";
    private static final String SQL_DELETE_SIZE = """
            DELETE FROM item_sizes WHERE item_size_id = ?""";
    @Override
    public List<ItemSize> findAllSizesForItem(int itemId) throws DaoException {
        List<ItemSize> sizeList = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_SIZES_FOR_ITEM)) {
            statement.setInt(1, itemId);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    int amount = resultSet.getInt(3);
                    ItemSize size = new ItemSize(id, name, amount);
                    sizeList.add(size);
                }
                return sizeList;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<ItemSize> findAll() throws DaoException {
        List<ItemSize> sizeList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                ItemSize size = new ItemSize(id, name);
                sizeList.add(size);
            }
            return sizeList;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<ItemSize> findEntityById(Integer sizeId) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_SELECT_SIZE_BY_ID)) {
            statement.setInt(1, sizeId);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    return Optional.of(new ItemSize(id, name));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(ItemSize entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_DELETE_SIZE)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(int sizeId, int itemId) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ITEM_SIZE)) {
            statement.setInt(1, sizeId);
            statement.setInt(2, itemId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean create(ItemSize entity) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_CREATE_SIZE)) {
            statement.setString(1, entity.getSizeName());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean createItemSize(ItemSize entity, int itemId) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_CREATE_ITEM_SIZE)) {
            statement.setInt(1, entity.getId());
            statement.setInt(2, itemId);
            statement.setInt(3, entity.getAmountInStock());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(ItemSize entity) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_SIZE)) {
            statement.setString(1, entity.getSizeName());
            statement.setInt(2, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean updateItemSize(ItemSize entity, int itemId) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ITEM_SIZE)) {
            statement.setInt(1, entity.getAmountInStock());
            statement.setInt(2, entity.getId());
            statement.setInt(3, itemId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean changeAmountInStockForAllOrderItems(int orderId) throws DaoException {
        try (PreparedStatement selectStatement = connection.prepareStatement(SQL_SELECT_ALL_BY_ORDER_ID, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
             PreparedStatement updateStatement = connection.prepareStatement(SQL_UPDATE_AMOUNT_IN_STOCK)) {
            selectStatement.setInt(1, orderId);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    int itemId = resultSet.getInt(1);
                    int itemSizeId = resultSet.getInt(2);
                    int amount = resultSet.getInt(3);
                    int amountInStock = resultSet.getInt(4);
                    if (amountInStock < amount) {
                        return false;
                    }
                    int newAmount = amountInStock - amount;
                    updateStatement.setInt(1, newAmount);
                    updateStatement.setInt(2, itemId);
                    updateStatement.setInt(3, itemSizeId);
                    if (updateStatement.executeUpdate() <= 0) {
                        return false;
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean changeBackAmountInStockForAllOrderItems(int orderId) throws DaoException {
        try (PreparedStatement selectStatement = connection.prepareStatement(SQL_SELECT_ALL_BY_ORDER_ID, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
             PreparedStatement updateStatement = connection.prepareStatement(SQL_UPDATE_AMOUNT_IN_STOCK)) {
            selectStatement.setInt(1, orderId);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    int itemId = resultSet.getInt(1);
                    int itemSizeId = resultSet.getInt(2);
                    int amount = resultSet.getInt(3);
                    int amountInStock = resultSet.getInt(4);
                    int newAmount = amountInStock + amount;
                    updateStatement.setInt(1, newAmount);
                    updateStatement.setInt(2, itemId);
                    updateStatement.setInt(3, itemSizeId);
                    if (updateStatement.executeUpdate() <= 0) {
                        return false;
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isSizeNameUnique(String name) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_IS_SIZE_NAME_UNIQUE)) {
            statement.setString(1, name);
            return !statement.executeQuery().next();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isSizeNameUnique(String name, int id) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_IS_SIZE_NAME_UNIQUE_EXCEPT_SIZE_WITH_ID)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            return !statement.executeQuery().next();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }
}
