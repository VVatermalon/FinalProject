package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.impl.UserDaoImpl;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.service.BaseService;
import by.skarulskaya.finalproject.util.PasswordEncryptor;

import java.util.Map;
import java.util.Optional;

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
            Optional<User> user = userDao.findUserByEmailAndPassword(email, encryptedPassword);
            return user;
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    @Override
    public boolean registerUser(Map<String, String> mapData) throws ServiceException {
        UserDaoImpl userDao = new UserDaoImpl();
        return false;
    }
}
