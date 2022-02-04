package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.CustomerDao;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;
import by.skarulskaya.finalproject.model.mapper.impl.CustomerMapper;
import by.skarulskaya.finalproject.model.mapper.impl.UserMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class CustomerDaoImpl extends CustomerDao {
    private static final String SQL_CREATE_CUSTOMER = """
        INSERT INTO customers(customer_id, user_id, bank_account, phone_number, default_address_id) 
        VALUES(?,?,?,?,?)""";
    private static final String SQL_FIND_CUSTOMER_BY_PHONE = """
        SELECT customer_id FROM customers WHERE phone_number = ?""";
    private static final String SQL_FIND_CUSTOMER_BY_ID = """
        SELECT C.customer_id, C.bank_account, C.phone_number, C.default_address_id, U.email, U.password, 
        U.name, U.surname, U.role, U.status 
        FROM customers C join users U on C.customer_id=U.user_id 
        WHERE C.customer_id = ?""";
    private static final EntityMapper<Customer> mapper = new CustomerMapper();

    @Override
    public List<Customer> findAll() throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Customer> findEntityById(Integer id) throws DaoException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_CUSTOMER_BY_ID)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Customer customer = mapper.map(resultSet);
                return Optional.of(customer);
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        } finally {
            close(resultSet);
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
    public boolean findCustomerByPhone(String phoneNumber) throws DaoException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_CUSTOMER_BY_PHONE)) {
            statement.setString(1, phoneNumber);
            resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        } finally {
            close(resultSet);
        }
    }
}
