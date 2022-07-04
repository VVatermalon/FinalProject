package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public abstract class CustomerDao extends AbstractDao<Integer, Customer> {
    public abstract boolean findCustomerByPhone(String phoneNumber) throws DaoException;
    public abstract boolean updateBankAccount(BigDecimal money, int customerId) throws DaoException;
    public abstract boolean updateDefaultAddress(int addressId, int customerId) throws DaoException;

    public abstract List<Customer> findAllByPage(int count, int offset) throws DaoException;

    public abstract List<Customer> findAllByStatusByPage(User.Status status, int count, int offset) throws DaoException;

    public abstract boolean updatePhoneNumber(int id, String phoneNumber) throws DaoException;
}
