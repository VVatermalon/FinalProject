package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.exception.DaoException;

import java.util.List;
import java.util.Optional;

public abstract class UserDao extends AbstractDao<Integer, User> {
    public abstract List<User> findAllAdminsByPage(int count, int offset) throws DaoException;

    public abstract List<User> findAllAdminsByStatusByPage(User.Status status, int count, int offset) throws DaoException;

    public abstract Optional<User> findUserByEmailAndPassword(String email, String password) throws DaoException;
    public abstract boolean findUserByEmail(String email) throws DaoException;

    public abstract String findUserEmail(int userId) throws DaoException;

    public abstract boolean checkCorrectPassword(int id, String password) throws DaoException;
    public abstract boolean updateName(int id, String name) throws DaoException;
    public abstract boolean updateSurname(int id, String surname) throws DaoException;
    public abstract boolean updatePassword(int id, String password) throws DaoException;

    public abstract boolean updateStatus(int id, User.Status status) throws DaoException;
}
