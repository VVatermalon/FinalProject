package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.Customer;

public abstract class CustomerDao extends AbstractDao<Integer, Customer> {
    public abstract boolean findCustomerByPhone(String phoneNumber) throws DaoException;
}
