package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.Customer;

import java.math.BigDecimal;

public abstract class CustomerDao extends AbstractDao<Integer, Customer> {
    public abstract boolean findCustomerByPhone(String phoneNumber) throws DaoException;
    public abstract boolean updateBankAccount(BigDecimal money, int customerId) throws DaoException;
    public abstract boolean updateDefaultAddress(int addressId, int customerId) throws DaoException;
    public abstract boolean updatePhoneNumber(int id, String phoneNumber) throws DaoException;
}
