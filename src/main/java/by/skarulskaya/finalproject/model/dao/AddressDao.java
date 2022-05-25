package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.Address;

import java.util.Optional;

public abstract class AddressDao extends AbstractDao<Integer, Address> {
    public abstract Optional<Address> findAddressByOrderId(Integer id) throws DaoException;
    public abstract Optional<Address> findAddress(Address address) throws DaoException;
}
