package by.skarulskaya.finalproject.model.service;

import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Address;

import java.util.Map;
import java.util.Optional;

public interface AddressService {
    Optional<Address> createAddress(Map<String, String> map, int customerId) throws ServiceException;
}
