package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.OrderComponentDao;
import by.skarulskaya.finalproject.model.entity.OrderComponent;
import by.skarulskaya.finalproject.model.entity.User;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderComponentDaoImpl extends OrderComponentDao {
    private static final String SQL_FIND_ALL_ORDER_COMPONENTS = """
        SELECT order_id, item_id, item_size_id, amount
        FROM orders_items WHERE order_id = ?""";
    private static final String SQL_CREATE_ORDER_COMPONENT = """
        INSERT INTO orders_items(order_id, item_id, item_size_id, amount) 
        VALUES(?,?,?,?)""";
    private static final String SQL_DELETE_ORDER_COMPONENT_BY_KEY = """
        DELETE FROM orders_items WHERE order_id = ? AND item_id = ? AND item_size_id = ?""";
    private static final String SQL_UPDATE_ORDER_COMPONENT = """
        UPDATE orders_items SET amount = ? WHERE order_id = ? AND item_id = ? AND item_size_id = ?""";
    private static final String SQL_FIND_ITEM_AMOUNT_BY_KEY = """
        SELECT amount FROM orders_items WHERE order_id = ? AND item_id = ? AND item_size_id = ?""";
    private static final String SQL_FIND_ITEM_AMOUNT_IN_ORDER = """
        SELECT SUM(amount) FROM orders_items GROUP BY order_id HAVING order_id = ?""";
    private static final String SQL_FIND_CART_TOTAL_PRICE = """
        SELECT OI.amount, I.price FROM orders_items OI JOIN items I ON OI.item_id = I.item_id
        WHERE order_id = ?""";
    private static final String SQL_FIND_NOT_ENOUGH_IN_STOCK_ITEMS_IN_ORDER = """
        SELECT * FROM orders_items OI JOIN items I ON OI.item_id = I.item_id
        JOIN items_item_sizes S ON I.item_id = S.item_id
        WHERE OI.order_id = ? AND S.item_size_id = OI.item_size_id AND S.amount_in_stock < OI.amount""";

    @Override
    public List<OrderComponent> findAll() throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<OrderComponent> findEntityById(OrderComponent.OrderComponentKey id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(OrderComponent entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(OrderComponent.OrderComponentKey id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ORDER_COMPONENT_BY_KEY)) {
            statement.setInt(1, id.getOrderId());
            statement.setInt(2, id.getItemId());
            statement.setInt(3, id.getItemSizeId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean create(OrderComponent entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean update(OrderComponent entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean createByKey(OrderComponent.OrderComponentKey key, int amount) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_ORDER_COMPONENT)) {
            statement.setInt(1, key.getOrderId());
            statement.setInt(2, key.getItemId());
            statement.setInt(3, key.getItemSizeId());
            statement.setInt(4, amount);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public HashMap<OrderComponent.OrderComponentKey, Integer> findAllOrderComponents(int orderId) throws DaoException {
        HashMap<OrderComponent.OrderComponentKey, Integer> cart = new HashMap<>();
        try(PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_ORDER_COMPONENTS)) {
            statement.setInt(1, orderId);
            try(ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    int itemId = resultSet.getInt(2);
                    int sizeId = resultSet.getInt(3);
                    int amount = resultSet.getInt(4);
                    OrderComponent.OrderComponentKey key = new OrderComponent.OrderComponentKey(orderId, itemId, sizeId);
                    cart.put(key, amount);
                }
                return cart;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean changeItemAmountInCart(OrderComponent.OrderComponentKey key, int amount) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ORDER_COMPONENT)) {
            statement.setInt(1, amount);
            statement.setInt(2, key.getOrderId());
            statement.setInt(3, key.getItemId());
            statement.setInt(4, key.getItemSizeId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public int findItemAmountInCart(OrderComponent.OrderComponentKey key) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_FIND_ITEM_AMOUNT_BY_KEY)) {
            statement.setInt(1, key.getOrderId());
            statement.setInt(2, key.getItemId());
            statement.setInt(3, key.getItemSizeId());
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public int countItemsInCart(int cartOrderId) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_FIND_ITEM_AMOUNT_IN_ORDER)) {
            statement.setInt(1, cartOrderId);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public BigDecimal findCartTotalPrice(int cartOrderId) throws DaoException {
        BigDecimal result = BigDecimal.ZERO;
        try(PreparedStatement statement = connection.prepareStatement(SQL_FIND_CART_TOTAL_PRICE)) {
            statement.setInt(1, cartOrderId);
            try(ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    int amount =  resultSet.getInt(1);
                    BigDecimal price = resultSet.getBigDecimal(2);
                    result = result.add(price.multiply(BigDecimal.valueOf(amount)));
                }
                return result;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean findNotEnoughInStockItemsInOrder(int cartOrderId) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement(SQL_FIND_NOT_ENOUGH_IN_STOCK_ITEMS_IN_ORDER)) {
            statement.setInt(1, cartOrderId);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }
}
