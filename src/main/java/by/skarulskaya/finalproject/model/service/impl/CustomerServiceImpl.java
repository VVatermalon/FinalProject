package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.CustomerDao;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.UserDao;
import by.skarulskaya.finalproject.model.dao.impl.CustomerDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.UserDaoImpl;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.service.CustomerService;
import by.skarulskaya.finalproject.util.encryptor.PasswordEncryptor;
import by.skarulskaya.finalproject.util.mail.MailSender;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.skarulskaya.finalproject.controller.Parameters.*;

public class CustomerServiceImpl implements CustomerService {
    private static final String REGISTRATION_SUBJECT = "Registration in system";
    private static final String REGISTRATION_BODY = "Welcome! Registration was successful";
    private static final String SETTING_CHANGED_SUBJECT = "Settings have changed";
    private static final String SETTING_CHANGED_BODY = "Dear Customer! Your settings have changed. You can find more information in your profile :)";
    private static final CustomerServiceImpl INSTANCE = new CustomerServiceImpl();
    private CustomerServiceImpl() {}
    public static CustomerServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Customer> findAll() throws ServiceException {
        CustomerDao customerDao = new CustomerDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(customerDao);
            return customerDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Customer> findAllByPage(int count, int offset) throws ServiceException {
        CustomerDao customerDao = new CustomerDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(customerDao);
            return customerDao.findAllByPage(count, offset);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Customer> findAllByStatusByPage(String status, int count, int offset) throws ServiceException {
        CustomerDao customerDao = new CustomerDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(customerDao);
            List<Customer> customers;
            if (status == null) {
                customers = customerDao.findAllByPage(count, offset);
            }
            else {
                User.Status userStatus = User.Status.valueOf(status.toUpperCase());
                customers = customerDao.findAllByStatusByPage(userStatus, count, offset);
            }
            return customers;
        } catch (DaoException | IllegalArgumentException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<Customer> findCustomerById(int id) throws ServiceException {
        CustomerDao customerDao = new CustomerDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(customerDao);
            return customerDao.findEntityById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean registerCustomer(Map<String, String> mapData) throws ServiceException {
        if(!BaseValidatorImpl.getInstance().validateRegistration(mapData)) {
            return false;
        }
        String email = mapData.get(USER_EMAIL);
        String password = mapData.get(USER_PASSWORD);
        String encryptedPassword = PasswordEncryptor.encrypt(password);
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
                sendMessage(email, REGISTRATION_SUBJECT, REGISTRATION_BODY);
            } catch(ServiceException e) {
                mapData.put(USER_EMAIL, WRONG_EMAIL);
                return false;
            }
            Customer customer = new Customer(BigDecimal.ZERO, phoneNumber, Optional.empty(), email, encryptedPassword, name, surname, User.Role.CUSTOMER, User.Status.ACTIVE);
            userDao.create(customer);
            customerDao.create(customer);
            transaction.commit();
            mapData.put(USER_ID, String.valueOf(customer.getId()));
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean addMoneyToAccount(double money, Customer customer) throws ServiceException {
        BigDecimal bankAccount = customer.getBankAccount();
        if(bankAccount.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            return false;
        }
        BigDecimal newBankAccount = bankAccount.add(BigDecimal.valueOf(money));
        newBankAccount = newBankAccount.min(BigDecimal.valueOf(1000));
        if(updateBankAccount(newBankAccount, customer.getId())) {
            customer.setBankAccount(newBankAccount);
            return true;
        }
        throw new ServiceException("Can't add money to account");
    }

    @Override
    public boolean updatePhoneNumber(Map<String, String> mapData) throws ServiceException {
        String phoneNumber = mapData.get(USER_PHONE_NUMBER);
        int userId = Integer.parseInt(mapData.get(USER_ID));
        if (!BaseValidatorImpl.getInstance().validatePhoneNumber(phoneNumber)) {
            mapData.put(USER_PHONE_NUMBER, INVALID_PHONE_NUMBER);
            return false;
        }
        CustomerDao customerDao = new CustomerDaoImpl();
        UserDao userDao = new UserDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(customerDao, userDao);
            if (customerDao.findCustomerByPhone(phoneNumber)) {
                mapData.put(USER_PHONE_NUMBER, NOT_UNIQUE_PHONE);
                return false;
            }
            if (customerDao.updatePhoneNumber(userId, phoneNumber)) {
                String email = userDao.findUserEmail(userId);
                sendMessage(email, SETTING_CHANGED_SUBJECT, SETTING_CHANGED_BODY);
                transaction.commit();
                return true;
            }
            transaction.rollback();
            throw new ServiceException("Can't update phone number, user id = " + userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private void sendMessage(String email, String subject, String body) throws ServiceException {
        try {
            MailSender.INSTANCE.send(email, subject, body);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private boolean updateBankAccount(BigDecimal newBankAccount, int customerId) throws ServiceException {
        CustomerDao customerDao = new CustomerDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(customerDao);
            return customerDao.updateBankAccount(newBankAccount, customerId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
