package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.model.dao.CustomerDao;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.UserDao;
import by.skarulskaya.finalproject.model.dao.impl.CustomerDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.UserDaoImpl;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.service.BaseService;
import by.skarulskaya.finalproject.util.encryptor.PasswordEncryptor;
import by.skarulskaya.finalproject.util.mail.MailSender;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static by.skarulskaya.finalproject.controller.Parameters.*;

public class UserService implements BaseService {
    private static final UserService INSTANCE = new UserService();
    private UserService() {}
    public static UserService getInstance() {
        return INSTANCE;
    }
    @Override
    public Optional<User> signIn(String email, String password) throws ServiceException {
        if(!BaseValidatorImpl.INSTANCE.validateSignIn(email, password)) {
            return Optional.empty();
        }
        String encryptedPassword = PasswordEncryptor.encrypt(password);
        return findUserByEmailAndPassword(email, encryptedPassword);
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) throws ServiceException {
        UserDaoImpl userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            return userDao.findUserByEmailAndPassword(email, password);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<User> findUserById(int id) throws ServiceException {
        UserDaoImpl userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            return userDao.findEntityById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean updateName(int userId, String newName) throws ServiceException {
        if(!BaseValidatorImpl.INSTANCE.validateName(newName)) {
            return false;
        }
        UserDao userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            if(userDao.updateName(userId, newName)) {
                String email = userDao.findUserEmail(userId);
                sendMessageSettingsChanged(email);
                return true;
            }
            throw new ServiceException("Can't update name, user id = " + userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean updateSurname(int userId, String newSurname) throws ServiceException {
        if(!BaseValidatorImpl.INSTANCE.validateName(newSurname)) {
            return false;
        }
        UserDao userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            if(userDao.updateSurname(userId, newSurname)) {
                String email = userDao.findUserEmail(userId);
                sendMessageSettingsChanged(email);
                return true;
            }
            throw new ServiceException("Can't update surname, user id = " + userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean checkCorrectPassword(int userId, String oldPassword) throws ServiceException {
        if(!BaseValidatorImpl.INSTANCE.validatePassword(oldPassword)) {
            return false;
        }
        UserDao userDao = new UserDaoImpl();
        String encryptedPassword = PasswordEncryptor.encrypt(oldPassword);
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            return userDao.checkCorrectPassword(userId, encryptedPassword);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean updatePassword(int userId, String newPassword) throws ServiceException {
        if(!BaseValidatorImpl.INSTANCE.validatePassword(newPassword)) {
            return false;
        }
        UserDao userDao = new UserDaoImpl();
        String encryptedPassword = PasswordEncryptor.encrypt(newPassword);
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            if(userDao.updatePassword(userId, encryptedPassword)) {
                String email = userDao.findUserEmail(userId);
                sendMessageSettingsChanged(email);
                return true;
            }
            throw new ServiceException("Can't update password, user id = " + userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private void sendMessageSettingsChanged(String email) throws ServiceException {
        try {
            MailSender.INSTANCE.send(email, "Settings have changed", "Dear Customer! Your settings have changed. You can find more information in your profile :)");//todo сделать тут локализацию
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
