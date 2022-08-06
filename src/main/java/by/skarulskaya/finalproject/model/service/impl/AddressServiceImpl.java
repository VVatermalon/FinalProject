package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.AddressDao;
import by.skarulskaya.finalproject.model.dao.CustomerDao;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.impl.AddressDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.CustomerDaoImpl;
import by.skarulskaya.finalproject.model.entity.Address;
import by.skarulskaya.finalproject.model.service.AddressService;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

import static by.skarulskaya.finalproject.controller.Parameters.*;

public class AddressServiceImpl implements AddressService {
    private static final Logger logger = LogManager.getLogger();
    private static final AddressServiceImpl INSTANCE = new AddressServiceImpl();

    private AddressServiceImpl() {
    }

    public static AddressServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Address> createAddress(Map<String, String> map, int customerId) throws ServiceException {
        if (!BaseValidatorImpl.getInstance().validateAddress(map)) {
            return Optional.empty();
        }
        Address.AvailableCountries country = Address.AvailableCountries.valueOf(map.get(COUNTRY).toUpperCase());
        String saveAddress = map.get(SAVE_FOR_LATER);
        String city = map.get(CITY);
        String address = map.get(ADDRESS);
        String apartmentTest = map.get(APARTMENT);
        String apartment = apartmentTest == null || apartmentTest.isBlank() ? null : apartmentTest;
        String postalCode = map.get(POSTAL_CODE);
        Address fullAddress = new Address(country, city, address, Optional.ofNullable(apartment), postalCode);
        if (saveAddress != null) {
            return createAndSaveAddress(fullAddress, customerId);
        }
        return createAddress(fullAddress);
    }

    private Optional<Address> createAndSaveAddress(Address address, int customerId) throws ServiceException {
        AddressDao addressDao = new AddressDaoImpl();
        CustomerDao customerDao = new CustomerDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(addressDao, customerDao);
            Optional<Address> addressOpt = addressDao.findAddress(address);
            if (addressOpt.isPresent()) {
                if (customerDao.updateDefaultAddress(addressOpt.get().getId(), customerId)) {
                    transaction.commit();
                    return addressOpt;
                }
                logger.error("Can't update customer default address");
                throw new ServiceException("Can't update customer default address, customerId = " +
                        customerId + ", addressId = " + addressOpt.get().getId());
            }
            addressDao.create(address);
            if (customerDao.updateDefaultAddress(address.getId(), customerId)) {
                transaction.commit();
                return Optional.of(address);
            }
            logger.error("Can't update customer default address");
            throw new ServiceException("Can't update customer default address, customerId = " +
                    customerId + ", addressId = " + address.getId());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private Optional<Address> createAddress(Address address) throws ServiceException {
        AddressDao addressDao = new AddressDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(addressDao);
            Optional<Address> addressOpt = addressDao.findAddress(address);
            if (addressOpt.isPresent()) {
                return addressOpt;
            }
            addressDao.create(address);
            return Optional.of(address);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
