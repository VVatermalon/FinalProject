package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.UserDao;
import by.skarulskaya.finalproject.model.dao.impl.UserDaoImpl;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.service.UserService;
import by.skarulskaya.finalproject.util.encryptor.PasswordEncryptor;
import by.skarulskaya.finalproject.util.mail.MailSender;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.skarulskaya.finalproject.controller.Parameters.*;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static final UserServiceImpl INSTANCE = new UserServiceImpl();
    private static final String SETTING_CHANGED_SUBJECT = "Settings have changed";
    private static final String SETTING_CHANGED_BODY = "Dear Customer! Your settings have changed. You can find more information in your profile :)";
    private static final String REGISTRATION_SUBJECT = "Registration in system";
    private static final String REGISTRATION_BODY_1 = "Welcome! Registration was successful. Your password: ";
    private static final String REGISTRATION_BODY_2 = ". Change it as you log in.";
    private static final String BLOCKED_SUBJECT = "You have been blocked";
    private static final String BLOCKED_BODY = "Sorry, You have been blocked by the Administrator :(";
    private static final String UNBLOCKED_SUBJECT = "You have been unblocked";
    private static final String UNBLOCKED_BODY = "You have been unblocked by the Administrator :) Now you can continue to use of Blackpink Web Site!";
    private UserServiceImpl() {}
    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<User> signIn(String email, String password) throws ServiceException {
        if(!BaseValidatorImpl.getInstance().validateSignIn(email, password)) {
            return Optional.empty();
        }
        String encryptedPassword = PasswordEncryptor.encrypt(password);
        return findUserByEmailAndPassword(email, encryptedPassword);
    }

    @Override
    public boolean registerAdmin(Map<String, String> mapData) throws ServiceException {
        if(!BaseValidatorImpl.getInstance().validateRegistrationAdmin(mapData)) {
            return false;
        }
        String email = mapData.get(USER_EMAIL);
        String password = mapData.get(USER_PASSWORD);
        String encryptedPassword = PasswordEncryptor.encrypt(password);
        String name = mapData.get(USER_NAME);
        String surname = mapData.get(USER_SURNAME);
        UserDao userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            if(userDao.findUserByEmail(email)) {
                mapData.put(USER_EMAIL, NOT_UNIQUE_EMAIL);
                return false;
            }
            try {
                sendMessage(email, REGISTRATION_SUBJECT, REGISTRATION_BODY_1 + password + REGISTRATION_BODY_2);
            } catch(ServiceException e) {
                mapData.put(USER_EMAIL, WRONG_EMAIL);
                return false;
            }
            User admin = new User(email, encryptedPassword, name, surname, User.Role.ADMIN, User.Status.IN_REGISTRATION_PROCESS);
            userDao.create(admin);
            return true;
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findAllAdminsByPage(int count, int offset) throws ServiceException {
        UserDao userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            return userDao.findAllAdminsByPage(count, offset);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findAllAdminsByStatusByPage(String status, int count, int offset) throws ServiceException {
        UserDao userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            List<User> users;
            if (status == null) {
                users = userDao.findAllAdminsByPage(count, offset);
            }
            else {
                User.Status userStatus = User.Status.valueOf(status.toUpperCase());
                users = userDao.findAllAdminsByStatusByPage(userStatus, count, offset);
            }
            return users;
        } catch (DaoException | IllegalArgumentException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password) throws ServiceException {
        UserDaoImpl userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            return userDao.findUserByEmailAndPassword(email, password);
        } catch (DaoException e) {
            logger.error(e);
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
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean updateName(int userId, String newName) throws ServiceException {
        if(!BaseValidatorImpl.getInstance().validateName(newName)) {
            return false;
        }
        UserDao userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            if(userDao.updateName(userId, newName)) {
                String email = userDao.findUserEmail(userId);
                sendMessage(email, SETTING_CHANGED_SUBJECT, SETTING_CHANGED_BODY);
                return true;
            }
            logger.error("Can't update name");
            throw new ServiceException("Can't update name, user id = " + userId);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean updateSurname(int userId, String newSurname) throws ServiceException {
        if(!BaseValidatorImpl.getInstance().validateName(newSurname)) {
            return false;
        }
        UserDao userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            if(userDao.updateSurname(userId, newSurname)) {
                String email = userDao.findUserEmail(userId);
                sendMessage(email, SETTING_CHANGED_SUBJECT, SETTING_CHANGED_BODY);
                return true;
            }
            logger.error("Can't update surname");
            throw new ServiceException("Can't update surname, user id = " + userId);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean checkCorrectPassword(int userId, String oldPassword) throws ServiceException {
        if(!BaseValidatorImpl.getInstance().validatePassword(oldPassword)) {
            return false;
        }
        UserDao userDao = new UserDaoImpl();
        String encryptedPassword = PasswordEncryptor.encrypt(oldPassword);
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            return userDao.checkCorrectPassword(userId, encryptedPassword);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean deleteUser(int userId) throws ServiceException {
        UserDao userDao = new UserDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            return userDao.delete(userId);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) throws ServiceException {
        if(!BaseValidatorImpl.getInstance().validatePassword(newPassword)) {
            return false;
        }
        UserDao userDao = new UserDaoImpl();
        String encryptedPassword = PasswordEncryptor.encrypt(newPassword);
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            if(userDao.updatePassword(userId, encryptedPassword)) {
                String email = userDao.findUserEmail(userId);
                sendMessage(email, SETTING_CHANGED_SUBJECT, SETTING_CHANGED_BODY);
                userDao.updateStatus(userId, User.Status.ACTIVE);
                return true;
            }
            logger.error("Can't update password");
            throw new ServiceException("Can't update password, user id = " + userId);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean changeUserStatus(int userId, User.Status userStatus) throws ServiceException {
        UserDao userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            if(userDao.updateStatus(userId, userStatus)) {
                String email = userDao.findUserEmail(userId);
                if(userStatus == User.Status.BLOCKED) {
                    sendMessage(email, BLOCKED_SUBJECT, BLOCKED_BODY);
                }
                if(userStatus == User.Status.ACTIVE) {
                    sendMessage(email, UNBLOCKED_SUBJECT, UNBLOCKED_BODY);
                }
                return true;
            }
            logger.error("Can't update status");
            throw new ServiceException("Can't update status, user id = " + userId);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    private void sendMessage(String email, String subject, String body) throws ServiceException {
        try {
            MailSender.INSTANCE.send(email, subject, body);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }
}
