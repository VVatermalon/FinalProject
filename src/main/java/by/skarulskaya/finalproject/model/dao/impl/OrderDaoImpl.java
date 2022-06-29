package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.OrderDao;
import by.skarulskaya.finalproject.model.entity.Order;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;
import by.skarulskaya.finalproject.model.mapper.impl.OrderMapper;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl extends OrderDao {
    private static final String SQL_CREATE_ORDER = """
        INSERT INTO orders(customer_id, status) VALUES(?,?)""";
    private static final String SQL_REGISTER_ORDER = """
        UPDATE orders SET status = ?, date_ordered = ?, shipping_address = ?, total_price = ? WHERE order_id = ?""";
    private static final String SQL_UPDATE_ORDER_STATUS = """
        UPDATE orders SET status = ? WHERE order_id = ?""";
    private static final String SQL_FIND_CART_ORDER_ID = """
        SELECT order_id FROM orders
        WHERE customer_id = ? AND status = 'IN_PROCESS'""";
    private static final String SQL_FIND_ALL_CUSTOMER_REGISTERED_ORDERS = """
        SELECT O.order_id, NULL AS customer_id, O.status AS order_status, 
        O.date_ordered, O.shipping_address, O.gift_card, O.total_price,
        A.address_id, A.country, A.city, A.address, A.apartment, A.postal_code
        FROM orders O JOIN addresses A ON O.shipping_address = A.address_id
        WHERE customer_id = ? AND status <> 'IN_PROCESS'""";
    private static final String SQL_FIND_ALL_REGISTERED_ORDERS = """
        SELECT O.order_id, O.customer_id, O.status AS order_status, O.date_ordered, O.shipping_address, 
        O.gift_card, O.total_price, C.bank_account, C.phone_number, NULL AS default_address_id, 
        U.email, U.password, U.name, U.surname, U.role, U.status, 
        A.address_id, A.country, A.city, A.address, A.apartment, A.postal_code
        FROM orders O JOIN customers C ON O.customer_id = C.customer_id
        JOIN users U ON C.customer_id = U.user_id
        JOIN addresses A ON O.shipping_address = A.address_id
        WHERE O.status <> 'IN_PROCESS'""";
    private static final String SQL_FIND_ALL_REGISTERED_ORDERS_LIMIT = """
        SELECT O.order_id, O.customer_id, O.status AS order_status, O.date_ordered, O.shipping_address, 
        O.gift_card, O.total_price, C.bank_account, C.phone_number, NULL AS default_address_id, 
        U.email, U.password, U.name, U.surname, U.role, U.status, 
        A.address_id, A.country, A.city, A.address, A.apartment, A.postal_code
        FROM orders O JOIN customers C ON O.customer_id = C.customer_id
        JOIN users U ON C.customer_id = U.user_id
        JOIN addresses A ON O.shipping_address = A.address_id
        WHERE O.status <> 'IN_PROCESS'
        ORDER BY O.order_id DESC
        LIMIT ? OFFSET ?""";
    private static final String SQL_FIND_REGISTERED_ORDERS_BY_DATE_LIMIT = """
        SELECT O.order_id, O.customer_id, O.status AS order_status, O.date_ordered, O.shipping_address, 
        O.gift_card, O.total_price, C.bank_account, C.phone_number, NULL AS default_address_id, 
        U.email, U.password, U.name, U.surname, U.role, U.status, 
        A.address_id, A.country, A.city, A.address, A.apartment, A.postal_code
        FROM orders O JOIN customers C ON O.customer_id = C.customer_id
        JOIN users U ON C.customer_id = U.user_id
        JOIN addresses A ON O.shipping_address = A.address_id
        WHERE O.date_ordered = ? AND O.status <> 'IN_PROCESS'
        ORDER BY O.order_id DESC
        LIMIT ? OFFSET ?""";
    private static final String SQL_FIND_REGISTERED_ORDERS_BY_STATUS_LIMIT = """
        SELECT O.order_id, O.customer_id, O.status AS order_status, O.date_ordered, O.shipping_address, 
        O.gift_card, O.total_price, C.bank_account, C.phone_number, NULL AS default_address_id, 
        U.email, U.password, U.name, U.surname, U.role, U.status, 
        A.address_id, A.country, A.city, A.address, A.apartment, A.postal_code
        FROM orders O JOIN customers C ON O.customer_id = C.customer_id
        JOIN users U ON C.customer_id = U.user_id
        JOIN addresses A ON O.shipping_address = A.address_id
        WHERE O.status = ? AND O.status <> 'IN_PROCESS'
        ORDER BY O.order_id DESC
        LIMIT ? OFFSET ?""";
    private static final String SQL_FIND_REGISTERED_ORDERS_BY_STATUS_BY_DATE_LIMIT = """
        SELECT O.order_id, O.customer_id, O.status AS order_status, O.date_ordered, O.shipping_address, 
        O.gift_card, O.total_price, C.bank_account, C.phone_number, NULL AS default_address_id, 
        U.email, U.password, U.name, U.surname, U.role, U.status, 
        A.address_id, A.country, A.city, A.address, A.apartment, A.postal_code
        FROM orders O JOIN customers C ON O.customer_id = C.customer_id
        JOIN users U ON C.customer_id = U.user_id
        JOIN addresses A ON O.shipping_address = A.address_id
        WHERE O.status = ? AND O.date_ordered = ? AND O.status <> 'IN_PROCESS'
        ORDER BY O.order_id DESC
        LIMIT ? OFFSET ?""";
    private static final String SQL_FIND_ORDER_CUSTOMER_EMAIL = """
        SELECT U.email
        FROM orders O JOIN users U ON O.customer_id = U.user_id
        WHERE O.order_id = ?""";
    private static final String SQL_ADD_MONEY_TO_ORDER_CUSTOMER_ACCOUNT = """
        UPDATE customers SET bank_account = bank_account + (
        SELECT total_price FROM orders WHERE order_id = ?) 
        WHERE customer_id = (SELECT customer_id FROM orders WHERE order_id = ?);""";
    private static final EntityMapper<Order> mapper = new OrderMapper();
    @Override
    public List<Order> findAll() throws DaoException {
        List<Order> orders = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_REGISTERED_ORDERS)) {
            while (resultSet.next()) {
                Order order = mapper.map(resultSet);
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Order> findAllByPage(int count, int offset) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_REGISTERED_ORDERS_LIMIT)) {
            statement.setInt(1, count);
            statement.setInt(2, offset);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = mapper.map(resultSet);
                    orders.add(order);
                }
            }
            return orders;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Order> findAllByDateByPage(LocalDate date, int count, int offset) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_REGISTERED_ORDERS_BY_DATE_LIMIT)) {
            statement.setDate(1, Date.valueOf(date.toString()));
            statement.setInt(2, count);
            statement.setInt(3, offset);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = mapper.map(resultSet);
                    orders.add(order);
                }
            }
            return orders;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Order> findAllByStatusByPage(Order.OrderStatus status, int count, int offset) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_REGISTERED_ORDERS_BY_STATUS_LIMIT)) {
            statement.setString(1, status.name());
            statement.setInt(2, count);
            statement.setInt(3, offset);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = mapper.map(resultSet);
                    orders.add(order);
                }
            }
            return orders;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Order> findAllByStatusByDateByPage(Order.OrderStatus status, LocalDate date, int count, int offset) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_REGISTERED_ORDERS_BY_STATUS_BY_DATE_LIMIT)) {
            statement.setString(1, status.name());
            statement.setDate(2, Date.valueOf(date.toString()));
            statement.setInt(3, count);
            statement.setInt(4, offset);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = mapper.map(resultSet);
                    orders.add(order);
                }
            }
            return orders;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Order> findAllCustomerRegisteredOrders(int customerId) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_CUSTOMER_REGISTERED_ORDERS)) {
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
            statement.setDate(2, Date.valueOf(LocalDate.now().toString()));
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

    @Override
    public String findOrderCustomerEmail(int orderId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ORDER_CUSTOMER_EMAIL)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(1);
                }
                throw new DaoException("Can't find customer's email, orderId = " + orderId);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean addMoneyToOrderCustomerAccount(int orderId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_ADD_MONEY_TO_ORDER_CUSTOMER_ACCOUNT)) {
            statement.setInt(1, orderId);
            statement.setInt(2, orderId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean changeOrderStatus(int orderId, Order.OrderStatus newStatus) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ORDER_STATUS)) {
            statement.setString(1, newStatus.name());
            statement.setInt(2, orderId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
