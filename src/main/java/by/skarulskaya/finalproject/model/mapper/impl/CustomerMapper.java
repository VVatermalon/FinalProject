package by.skarulskaya.finalproject.model.mapper.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.Address;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CustomerMapper implements EntityMapper<Customer> {
    private static final String ID_LABEL = "customer_id";
    private static final String BANK_ACCOUNT_LABEL = "bank_account";
    private static final String PHONE_LABEL = "phone_number";
    private static final String DEFAULT_ADDRESS_ID_LABEL = "default_address_id";
    private static final String EMAIL_LABEL = "email";
    private static final String PASSWORD_LABEL = "password";
    private static final String NAME_LABEL = "name";
    private static final String SURNAME_LABEL = "surname";
    private static final String ROLE_LABEL = "role";
    private static final String STATUS_LABEL = "status";

    @Override
    public Customer map(ResultSet resultSet) throws DaoException {
        try {
            int id = resultSet.getInt(ID_LABEL);
            BigDecimal bankAccount = resultSet.getBigDecimal(BANK_ACCOUNT_LABEL);
            String phone = resultSet.getString(PHONE_LABEL);
            String email = resultSet.getString(EMAIL_LABEL);
            String password = resultSet.getString(PASSWORD_LABEL);
            String name = resultSet.getString(NAME_LABEL);
            String surname = resultSet.getString(SURNAME_LABEL);
            String roleStr = resultSet.getString(ROLE_LABEL).trim();
            User.Role role = User.Role.valueOf(roleStr);
            String statusStr = resultSet.getString(STATUS_LABEL).trim();
            User.Status status = User.Status.valueOf(statusStr);
            int defaultAddressId = resultSet.getInt(DEFAULT_ADDRESS_ID_LABEL);
            Optional<Address> addressResult = Optional.empty();
            if (defaultAddressId != 0) {
                EntityMapper<Address> addressMapper = new AddressMapper();
                Address address = addressMapper.map(resultSet);
                addressResult = Optional.of(address);
            }
            return new Customer(id, bankAccount, phone, addressResult, email, password, name, surname, role, status);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
