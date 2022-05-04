package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.OrderDao;
import by.skarulskaya.finalproject.model.entity.Order;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl extends OrderDao {
    private static final String SQL_CREATE_ORDER = """
        INSERT INTO orders(customer_id, status) VALUES(?,?)""";
    private static final String SQL_REGISTER_ORDER = """
        UPDATE orders SET status = ?, date_ordered = ?, shipping_address = ? WHERE order_id = ?""";
    private static final String SQL_FIND_CART_ORDER_ID = """
        SELECT O.order_id
        FROM CUSTOMERS C JOIN orders O ON C.customer_id = O.customer_id
        WHERE C.customer_id = ? AND O.status = 'IN_PROCESS'""";
    @Override
    public List<Order> findAll() throws DaoException {
        throw new UnsupportedOperationException();
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
    public boolean registerOrder(int orderId, int addressId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_REGISTER_ORDER)) {
            statement.setString(1, Order.OrderStatus.NEED_CONFIRMATION.name());
            long now = System.currentTimeMillis();
            Date sqlDate = new Date(now);
            statement.setDate(2, sqlDate);
            statement.setInt(3, addressId);
            statement.setInt(4, orderId);
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

    @Override
    public List<Order> findAllCustomerRegisteredOrders(int customerId) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
