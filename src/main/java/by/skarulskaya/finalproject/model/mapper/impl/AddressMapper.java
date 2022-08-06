package by.skarulskaya.finalproject.model.mapper.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.Address;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AddressMapper implements EntityMapper<Address> {
    private static final Logger logger = LogManager.getLogger();
    private static final String ID_LABEL = "address_id";
    private static final String COUNTRY_LABEL = "country";
    private static final String CITY_LABEL = "city";
    private static final String ADDRESS_LABEL = "address";
    private static final String APARTMENT_LABEL = "apartment";
    private static final String POSTAL_CODE_LABEL = "postal_code";

    @Override
    public Address map(ResultSet resultSet) throws DaoException {
        try {
            int id = resultSet.getInt(ID_LABEL);
            String countryStr = resultSet.getString(COUNTRY_LABEL).trim();
            Address.AvailableCountries country = Address.AvailableCountries.valueOf(countryStr);
            String city = resultSet.getString(CITY_LABEL);
            String address = resultSet.getString(ADDRESS_LABEL);
            String apartment = resultSet.getString(APARTMENT_LABEL);
            String postalCode = resultSet.getString(POSTAL_CODE_LABEL);
            return new Address(id, country, city, address, Optional.ofNullable(apartment), postalCode);
        } catch (SQLException e) {
            logger.error("Address mapper error: " + e);
            throw new DaoException(e);
        }
    }
}
