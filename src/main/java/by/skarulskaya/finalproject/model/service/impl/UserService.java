package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.impl.UserDaoImpl;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.service.BaseService;
import by.skarulskaya.finalproject.util.encryptor.PasswordEncryptor;
import by.skarulskaya.finalproject.util.mail.MailSender;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;

import java.util.Map;
import java.util.Optional;

import static by.skarulskaya.finalproject.controller.Parameters.*;

public class UserService implements BaseService {
    private static UserService INSTANCE = new UserService();
    private UserService() {}
    public static UserService getInstance() {
        return INSTANCE;
    }
    @Override
    public Optional<User> signIn(String email, String password) throws ServiceException {
        UserDaoImpl userDao = new UserDaoImpl();
        String encryptedPassword = PasswordEncryptor.encrypt(password);
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            return userDao.findUserByEmailAndPassword(email, encryptedPassword);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean registerUser(Map<String, String> mapData) throws ServiceException {
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
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            if(userDao.findUserByEmail(email)) {
                mapData.put(USER_EMAIL, NOT_UNIQUE_EMAIL);
                return false;
            }
            //todo здесь еще проверка на номер из customer
            try {
                sendMessageRegistration(email);
            } catch(ServiceException e) {
                mapData.put(USER_EMAIL, WRONG_EMAIL); //todo подтверждение почти через ввод числа
                return false;
            }
            User user = new User(email, password, name, surname, User.Role.CUSTOMER, User.Status.IN_REGISTRATION_PROCESS); //todo после отправки письма статус другой
            userDao.create(user);
            Customer customer = new Customer(); //todo создавать связанного с юзером customer
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void sendMessageRegistration(String email) throws ServiceException {
        try {
            MailSender.INSTANCE.send(email, "Registration in system", "Welcome! Registration was successful"); //todo сделать тут локализацию
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
