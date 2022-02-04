package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.CustomerDao;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.impl.CustomerDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.UserDaoImpl;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.service.BaseService;
import by.skarulskaya.finalproject.util.encryptor.PasswordEncryptor;
import by.skarulskaya.finalproject.util.mail.MailSender;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static by.skarulskaya.finalproject.controller.Parameters.*;

public class CustomerService {
    private static CustomerService INSTANCE = new CustomerService();
    private CustomerService() {}
    public static CustomerService getInstance() {
        return INSTANCE;
    }

    public Optional<Customer> signIn(int id) throws ServiceException {
        CustomerDao customerDao = new CustomerDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(customerDao);
            return customerDao.findEntityById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean registerCustomer(Map<String, String> mapData) throws ServiceException {
        if(!BaseValidatorImpl.INSTANCE.validateRegistration(mapData)) {
            return false;
        }
        String email = mapData.get(USER_EMAIL);
        String password = mapData.get(USER_PASSWORD);
        password = PasswordEncryptor.encrypt(password);
        String name = mapData.get(USER_NAME);
        String surname = mapData.get(USER_SURNAME);
        String phoneNumber = mapData.get(USER_PHONE_NUMBER);
        UserDaoImpl userDao = new UserDaoImpl();
        CustomerDaoImpl customerDao = new CustomerDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(userDao, customerDao);
            if(userDao.findUserByEmail(email)) {
                mapData.put(USER_EMAIL, NOT_UNIQUE_EMAIL);
                return false;
            }
            if(customerDao.findCustomerByPhone(phoneNumber)) {
                mapData.put(USER_PHONE_NUMBER, NOT_UNIQUE_PHONE);
                return false;
            }
            try {
                sendMessageRegistration(email);
            } catch(ServiceException e) {
                mapData.put(USER_EMAIL, WRONG_EMAIL); //todo подтверждение почти через ввод числа
                return false;
            }
            Customer customer = new Customer(BigDecimal.ZERO, phoneNumber, Optional.empty(), email, password, name, surname, User.Role.CUSTOMER, User.Status.IN_REGISTRATION_PROCESS);
            //todo после отправки письма статус другой
            userDao.create(customer);
            customerDao.create(customer);
            transaction.commit();
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void sendMessageRegistration(String email) throws ServiceException {
        try {
            MailSender.INSTANCE.send(email, "Registration in system", "Welcome! Registration was successful"); //todo сделать тут локализацию
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
