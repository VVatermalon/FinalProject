package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.exception.DaoException;

import java.util.Optional;

public abstract class UserDao extends AbstractDao<Integer, User> {
    public abstract Optional<User> findUserByEmailAndPassword(String email, String password) throws DaoException;
    public abstract boolean findUserByEmail(String email) throws DaoException;
    public abstract boolean checkCorrectPassword(int id, String password) throws DaoException;
    public abstract boolean updateName(int id, String name) throws DaoException;
    public abstract boolean updateSurname(int id, String surname) throws DaoException;
    public abstract boolean updatePassword(int id, String password) throws DaoException;
}
