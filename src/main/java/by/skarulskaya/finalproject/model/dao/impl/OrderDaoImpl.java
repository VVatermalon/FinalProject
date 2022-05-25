package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.OrderDao;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.Order;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;
import by.skarulskaya.finalproject.model.mapper.impl.ItemMapper;
import by.skarulskaya.finalproject.model.mapper.impl.OrderMapper;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl extends OrderDao {
    private static final String SQL_CREATE_ORDER = """
        INSERT INTO orders(customer_id, status) VALUES(?,?)""";
    private static final String SQL_REGISTER_ORDER = """
        UPDATE orders SET status = ?, date_ordered = ?, shipping_address = ?, total_price = ? WHERE order_id = ?""";
    private static final String SQL_FIND_CART_ORDER_ID = """
        SELECT order_id FROM orders
        WHERE customer_id = ? AND status = 'IN_PROCESS'""";
    private static final String SQL_FIND_ALL_REGISTERED_ORDERS = """
        SELECT order_id, status, date_ordered, gift_card, total_price FROM orders
        WHERE customer_id = ? AND status <> 'IN_PROCESS'""";
    private static final EntityMapper<Order> mapper = new OrderMapper();
    @Override
    public List<Order> findAll() throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Order> findAllRegisteredOrders(int customerId) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_REGISTERED_ORDERS)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = mapper.map(resultSet);
                    orders.add(order);
                }
                return orders;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Order> findEntityById(Integer id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Order entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean create(Order entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean update(Order entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean registerOrder(int orderId, int addressId, BigDecimal cartTotalPrice) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_REGISTER_ORDER)) {
            statement.setString(1, Order.OrderStatus.NEED_CONFIRMATION.name());
            long now = System.currentTimeMillis();
            Date sqlDate = new Date(now);
            statement.setDate(2, sqlDate);
            statement.setInt(3, addressId);
            statement.setBigDecimal(4, cartTotalPrice);
            statement.setInt(5, orderId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int createCartOrder(int customerId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, customerId);
            statement.setString(2, Order.OrderStatus.IN_PROCESS.name());
            statement.executeUpdate();
            try(ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new DaoException("Smth wrong with generated id"); //todo is that possible?
                }
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int findCartOrderId(int customerId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_CART_ORDER_ID)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                throw new DaoException("Can't find customer's cart, customerId = " + customerId);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }
}
