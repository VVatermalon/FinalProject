package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.CustomerDao;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;
import by.skarulskaya.finalproject.model.mapper.impl.CustomerMapper;
import by.skarulskaya.finalproject.model.mapper.impl.UserMapper;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDaoImpl extends CustomerDao {
    private static final String SQL_CREATE_CUSTOMER = """
        INSERT INTO customers(customer_id, user_id, bank_account, phone_number, default_address_id) 
        VALUES(?,?,?,?,?)""";
    private static final String SQL_FIND_ALL_CUSTOMERS = """
        SELECT C.customer_id, C.bank_account, C.phone_number, C.default_address_id,
        U.email, U.password, U.name, U.surname, U.role, U.status, A.address_id, 
        A.country, A.city, A.address, A.apartment, A.postal_code
        FROM customers C JOIN users U ON C.customer_id=U.user_id 
        LEFT JOIN addresses A on C.default_address_id = A.address_id""";
    private static final String SQL_FIND_ALL_CUSTOMERS_BY_PAGE = """
        SELECT C.customer_id, C.bank_account, C.phone_number, C.default_address_id,
        U.email, U.password, U.name, U.surname, U.role, U.status, A.address_id, 
        A.country, A.city, A.address, A.apartment, A.postal_code
        FROM customers C JOIN users U ON C.customer_id = U.user_id 
        LEFT JOIN addresses A on C.default_address_id = A.address_id
        ORDER BY C.customer_id
        LIMIT ? OFFSET ?""";
    private static final String SQL_FIND_ALL_CUSTOMERS_BY_STATUS_BY_PAGE = """
        SELECT C.customer_id, C.bank_account, C.phone_number, C.default_address_id,
        U.email, U.password, U.name, U.surname, U.role, U.status, A.address_id, 
        A.country, A.city, A.address, A.apartment, A.postal_code
        FROM customers C JOIN users U ON C.customer_id = U.user_id 
        LEFT JOIN addresses A on C.default_address_id = A.address_id
        WHERE U.status = ?
        ORDER BY C.customer_id
        LIMIT ? OFFSET ?""";
    private static final String SQL_FIND_CUSTOMER_BY_PHONE = """
        SELECT customer_id FROM customers WHERE phone_number = ?""";
    private static final String SQL_FIND_CUSTOMER_BY_ID = """
        SELECT C.customer_id, C.bank_account, C.phone_number, C.default_address_id,
        U.email, U.password, U.name, U.surname, U.role, U.status, A.address_id, 
        A.country, A.city, A.address, A.apartment, A.postal_code
        FROM customers C JOIN users U ON C.customer_id=U.user_id 
        LEFT JOIN addresses A on C.default_address_id = A.address_id
        WHERE C.customer_id = ?""";
    private static final String SQL_UPDATE_BANK_ACCOUNT = """
        UPDATE customers SET bank_account = ?
        WHERE customer_id = ?""";
    private static final String SQL_UPDATE_PHONE_NUMBER = """
        UPDATE customers SET phone_number = ?
        WHERE customer_id = ?""";
    private static final String SQL_UPDATE_DEFAULT_ADDRESS = """
        UPDATE customers SET default_address_id = ?
        WHERE customer_id = ?""";
    private static final EntityMapper<Customer> mapper = new CustomerMapper();

    @Override
    public List<Customer> findAll() throws DaoException {
        List<Customer> customers = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_CUSTOMERS)) {
            while (resultSet.next()) {
                Customer customer = mapper.map(resultSet);
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Customer> findAllByPage(int count, int offset) throws DaoException {
        List<Customer> customers = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_CUSTOMERS_BY_PAGE)) {
            statement.setInt(1, count);
            statement.setInt(2, offset);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Customer customer = mapper.map(resultSet);
                    customers.add(customer);
                }
            }
            return customers;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Customer> findAllByStatusByPage(User.Status status, int count, int offset) throws DaoException {
        List<Customer> customers = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_CUSTOMERS_BY_STATUS_BY_PAGE)) {
            statement.setString(1, status.name());
            statement.setInt(2, count);
            statement.setInt(3, offset);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Customer customer = mapper.map(resultSet);
                    customers.add(customer);
                }
            }
            return customers;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Customer> findEntityById(Integer id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_CUSTOMER_BY_ID)) {
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Customer customer = mapper.map(resultSet);
                    return Optional.of(customer);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Customer entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean create(Customer entity) throws DaoException {
        ResultSet keys = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_CUSTOMER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entity.getId());
            statement.setInt(2, entity.getId());
            statement.setBigDecimal(3, entity.getBankAccount());
            statement.setString(4, entity.getPhoneNumber());
            if(entity.getDefaultAddress().isPresent()) {
                statement.setInt(5, entity.getDefaultAddress().get().getId());
            }
            else {
                statement.setNull(5, Types.INTEGER);
            }
            statement.executeUpdate();
            keys = statement.getGeneratedKeys();
            if (!keys.next()) {
                throw new DaoException("Smth wrong with generated id");
            }
            int id = keys.getInt(1);
            entity.setId(id);
            return true;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        } finally {
            close(keys);
        }
    }

    @Override
    public boolean update(Customer entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean updatePhoneNumber(int id, String phoneNumber) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_PHONE_NUMBER)) {
            statement.setString(1, phoneNumber);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean findCustomerByPhone(String phoneNumber) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_CUSTOMER_BY_PHONE)) {
            statement.setString(1, phoneNumber);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean updateBankAccount(BigDecimal money, int customerId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_BANK_ACCOUNT)) {
            statement.setBigDecimal(1, money);
            statement.setInt(2, customerId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean updateDefaultAddress(int addressId, int customerId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_DEFAULT_ADDRESS)) {
            statement.setInt(1, addressId);
            statement.setInt(2, customerId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }
}
