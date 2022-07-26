package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.model.dao.UserDao;
import by.skarulskaya.finalproject.model.entity.Customer;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;
import by.skarulskaya.finalproject.model.mapper.impl.UserMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl extends UserDao {
    private static final String SQL_SELECT_ALL_USERS = """
        SELECT user_id, email, password, name, surname, role, status FROM users""";
    private static final String SQL_FIND_ALL_ADMINS_BY_PAGE = """
        SELECT user_id, email, password, name, surname, role, status FROM users
        WHERE role = 'ADMIN'
        LIMIT ? OFFSET ?""";
    private static final String SQL_FIND_ALL_ADMINS_BY_STATUS_BY_PAGE = """
        SELECT user_id, email, password, name, surname, role, status FROM users
        WHERE role = 'ADMIN' AND status = ?
        LIMIT ? OFFSET ?""";
    private static final String SQL_FIND_USER_BY_ID = """
        SELECT user_id, email, password, name, surname, role, status 
        FROM users WHERE user_id = ?""";
    private static final String SQL_FIND_USER_BY_EMAIL = """
        SELECT user_id FROM users WHERE email = ?""";
    private static final String SQL_FIND_USER_EMAIL = """
        SELECT email FROM users WHERE user_id = ?""";
    private static final String SQL_FIND_USER_BY_EMAIL_AND_PASSWORD = """
        SELECT user_id, email, password, name, surname,role, status
        FROM users WHERE email = ? and password = ?""";
    private static final String SQL_FIND_USER_BY_ID_AND_PASSWORD = """
        Select user_id FROM users WHERE user_id = ? AND password = ?""";
    private static final String SQL_CREATE_USER = """
        INSERT INTO users(email, password, name, surname, role, status) 
        VALUES(?,?,?,?,?,?)""";
    private static final String SQL_UPDATE_USER = """
        UPDATE users SET email = ?, password = ?, name = ?, surname = ?, 
        role = ?, status = ? WHERE user_id = ?""";
    private static final String SQL_UPDATE_NAME = """
        UPDATE users SET name = ? WHERE user_id = ?""";
    private static final String SQL_UPDATE_SURNAME = """
        UPDATE users SET surname = ? WHERE user_id = ?""";
    private static final String SQL_UPDATE_PASSWORD = """
        UPDATE users SET password = ? WHERE user_id = ?""";
    private static final String SQL_UPDATE_STATUS = """
        UPDATE users SET status = ? WHERE user_id = ?""";
    private static final EntityMapper<User> mapper = new UserMapper();

    @Override
    public List<User> findAll() throws DaoException {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_USERS)) {
            while (resultSet.next()) {
                User user = mapper.map(resultSet);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> findAllAdminsByPage(int count, int offset) throws DaoException {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_ADMINS_BY_PAGE)) {
            statement.setInt(1, count);
            statement.setInt(2, offset);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = mapper.map(resultSet);
                    users.add(user);
                }
            }
            return users;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> findAllAdminsByStatusByPage(User.Status status, int count, int offset) throws DaoException {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_ADMINS_BY_STATUS_BY_PAGE)) {
            statement.setString(1, status.name());
            statement.setInt(2, count);
            statement.setInt(3, offset);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = mapper.map(resultSet);
                    users.add(user);
                }
            }
            return users;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findEntityById(Integer id) throws DaoException {
        Optional<User> output = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_USER_BY_ID)) {
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = mapper.map(resultSet);
                    output = Optional.of(user);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return output;
    }

    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password) throws DaoException {
        Optional<User> output = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_USER_BY_EMAIL_AND_PASSWORD)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = mapper.map(resultSet);
                    output = Optional.of(user);
                }
                return output;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean findUserByEmail(String email) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_USER_BY_EMAIL)) {
            statement.setString(1, email);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public String findUserEmail(int userId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_USER_EMAIL)) {
            statement.setInt(1, userId);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getString(1);
                }
                throw new DaoException("Can't find user's email, orderId = " + userId);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean checkCorrectPassword(int id, String password) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_USER_BY_ID_AND_PASSWORD)) {
            statement.setInt(1, id);
            statement.setString(2, password);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(User entity) throws DaoException {
        int user_id = entity.getId();
        return delete(user_id);
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_USER_BY_ID, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return false;
                }
                resultSet.deleteRow();
                return true;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean create(User entity) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getName());
            statement.setString(4, entity.getSurname());
            statement.setString(5, entity.getRole().name());
            statement.setString(6, entity.getStatus().name());
            statement.executeUpdate();
            try(ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new DaoException("Smth wrong with generated id");
                }
                int id = keys.getInt(1);
                entity.setId(id);
                return true;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(User entity) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_USER)) {
            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getName());
            statement.setString(4, entity.getSurname());
            statement.setString(5, entity.getRole().name());
            statement.setString(6, entity.getStatus().name());
            statement.setInt(7, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean updateName(int id, String name) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_NAME)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean updateSurname(int id, String surname) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_SURNAME)) {
            statement.setString(1, surname);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean updatePassword(int id, String password) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_PASSWORD)) {
            statement.setString(1, password);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean updateStatus(int id, User.Status status) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_STATUS)) {
            statement.setString(1, status.name());
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }
}
