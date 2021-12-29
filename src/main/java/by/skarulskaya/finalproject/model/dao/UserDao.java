package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.exception.DaoException;

import java.util.Optional;

public abstract class UserDao extends AbstractDao<Integer, User> {
    public abstract Optional<User> findUserByEmailAndPassword(String email, String password) throws DaoException;
}
