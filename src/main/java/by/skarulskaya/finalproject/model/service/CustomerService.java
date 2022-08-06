package by.skarulskaya.finalproject.model.service;

import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Customer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomerService {
    List<Customer> findAll() throws ServiceException;
    List<Customer> findAllByPage(int count, int offset) throws ServiceException;
    List<Customer> findAllByStatusByPage(String status, int count, int offset) throws ServiceException;
    Optional<Customer> findCustomerById(int id) throws ServiceException;
    boolean registerCustomer(Map<String, String> mapData) throws ServiceException;
    boolean addMoneyToAccount(double money, Customer customer) throws ServiceException;
    boolean updatePhoneNumber(Map<String, String> mapData) throws ServiceException;

}
