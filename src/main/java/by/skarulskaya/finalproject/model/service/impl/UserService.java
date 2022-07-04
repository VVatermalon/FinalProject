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
import java.util.List;
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

    public boolean registerAdmin(Map<String, String> mapData) throws ServiceException {
        if(!BaseValidatorImpl.INSTANCE.validateRegistrationAdmin(mapData)) {
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
                sendMessageRegistration(email, "Welcome! Registration was successful. Your password: " + password + ". Change it as you log in.");
            } catch(ServiceException e) {
                mapData.put(USER_EMAIL, WRONG_EMAIL); //todo подтверждение почты через ввод сгенерированной последовательности, храним их в бд и периодически чистим
                return false;
            }
            User admin = new User(email, encryptedPassword, name, surname, User.Role.ADMIN, User.Status.IN_REGISTRATION_PROCESS);
            //todo после отправки письма статус другой
            userDao.create(admin);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<User> findAllAdminsByPage(int count, int offset) throws ServiceException {
        UserDao userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            return userDao.findAllAdminsByPage(count, offset);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

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
            throw new ServiceException(e);
        }
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

    public boolean deleteUser(int userId) throws ServiceException {
        UserDao userDao = new UserDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            return userDao.delete(userId);
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
                userDao.updateStatus(userId, User.Status.ACTIVE);
                return true;
            }
            throw new ServiceException("Can't update password, user id = " + userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean changeUserStatus(int userId, User.Status userStatus) throws ServiceException {
        UserDao userDao = new UserDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(userDao);
            if(userDao.updateStatus(userId, userStatus)) {
                String email = userDao.findUserEmail(userId);
                if(userStatus == User.Status.BLOCKED) {
                    sendMessage(email, "You are blocked", "Sorry, You have been blocked by the Administrator :(");
                }
                if(userStatus == User.Status.ACTIVE) {
                    sendMessage(email, "You are unblocked", "You have been unblocked by the Administrator :) Now you can continue to use of our Web Site!");
                }
                return true;
            }
            throw new ServiceException("Can't update status, user id = " + userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private void sendMessageRegistration(String email, String mailText) throws ServiceException {
        try {
            MailSender.INSTANCE.send(email, "Registration in system", mailText); //todo сделать тут локализацию
        } catch (Exception e) {
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

    private void sendMessage(String email, String subject, String text) throws ServiceException {
        try {
            MailSender.INSTANCE.send(email, subject, text);//todo сделать тут локализацию
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
