package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.dao.AddressDao;
import by.skarulskaya.finalproject.model.entity.Address;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;
import by.skarulskaya.finalproject.model.mapper.impl.AddressMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class AddressDaoImpl extends AddressDao {
    private static final String SQL_FIND_ADDRESS_BY_ID = """
        SELECT address_id, country, city, address, apartment, postal_code
        FROM addresses where address_id = ?""";
    private static final String SQL_FIND_ADDRESS_BY_ORDER_ID = """
        SELECT A.address_id, A.country, A.city, A.address, A.apartment, A.postal_code 
        FROM addresses A JOIN orders O ON A.address_id = O.shipping_address WHERE O.order_id = ?""";
    private static final String SQL_FIND_ADDRESS = """
        SELECT address_id FROM addresses where country = ? AND city = ? AND
        address = ? AND postal_code = ? AND apartment = ?""";
    private static final String SQL_FIND_ADDRESS_APARTMENT_IS_NULL = """
        SELECT address_id FROM addresses where country = ? AND city = ? AND 
        address = ? AND postal_code = ? AND apartment IS NULL""";
    private static final String SQL_CREATE_ADDRESS = """
        INSERT INTO addresses(country, city, address, apartment, postal_code) 
        VALUES(?,?,?,?,?) """;
    private static final EntityMapper<Address> mapper = new AddressMapper();
    @Override
    public List<Address> findAll() throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Address> findEntityById(Integer id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ADDRESS_BY_ID)) {
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Address address = mapper.map(resultSet);
                    return Optional.of(address);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Address> findAddress(Address address) throws DaoException {
        String sqlQuery = SQL_FIND_ADDRESS_APARTMENT_IS_NULL;
        if(address.getApartment().isPresent()) {
            sqlQuery = SQL_FIND_ADDRESS;
        }
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, address.getCountry().name());
            statement.setString(2, address.getCity());
            statement.setString(3, address.getAddress());
            statement.setString(4, address.getPostalCode());
            if(address.getApartment().isPresent()) {
                statement.setString(5, address.getApartment().get());
            }
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    address.setId(resultSet.getInt(1));
                    return Optional.of(address);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Address entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean create(Address entity) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_ADDRESS, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getCountry().name());
            statement.setString(2, entity.getCity());
            statement.setString(3, entity.getAddress());
            if(entity.getApartment().isPresent()) {
                statement.setString(4, entity.getApartment().get());
            }
            else {
                statement.setNull(4, Types.VARCHAR);
            }
            statement.setString(5, entity.getPostalCode());
            statement.executeUpdate();
            try(ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new DaoException("Smth wrong with generated id"); //todo is that possible?
                }
                int id = keys.getInt(1);
                entity.setId(id);
                return true;
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Address entity) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
