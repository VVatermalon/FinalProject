package by.skarulskaya.finalproject.model.dao.impl;

import by.skarulskaya.finalproject.model.dao.UserDao;
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
    private static final String SQL_FIND_USER_BY_ID = """
        SELECT user_id, email, password, name, surname, role, status 
        FROM users WHERE user_id = ?""";
    private static final String SQL_FIND_USER_BY_LOGIN_AND_PASSWORD = """
        SELECT user_id, email, password, name, surname,role, status
        FROM users WHERE email = ? and password = ?""";
    private static final String SQL_CREATE_USER = """
        INSERT INTO users(email, password, name, surname, role, status) 
        VALUES(?,?,?,?,?,?)""";
    private static final String SQL_UPDATE_USER = """
        UPDATE users set email = ?, password = ?, name = ?, surname = ?, 
        role = ?, status = ? WHERE user_id = ?""";
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
        } catch (SQLException e) {
            logger.error("Sql exception: ", e);
            throw new DaoException(e);
        }
        return users;
    }

    @Override
    public Optional<User> findEntityById(Integer id) throws DaoException {
        ResultSet resultSet = null;
        Optional<User> output = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_USER_BY_ID)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = mapper.map(resultSet);
                output = Optional.of(user);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        } finally {
            close(resultSet);
        }
        return output;
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) throws DaoException {
        ResultSet resultSet = null;
        Optional<User> output = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_USER_BY_LOGIN_AND_PASSWORD)) {
            statement.setString(1, email);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = mapper.map(resultSet);
                output = Optional.of(user);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        } finally {
            close(resultSet);
        }
        return output;
    }

    @Override
    public boolean delete(User entity) throws DaoException {
        int user_id = entity.getId();
        return delete(user_id);
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_USER_BY_ID, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                resultSet.updateInt("status", User.Status.DELETED.ordinal());
                resultSet.updateRow();
                return true;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        } finally {
            close(resultSet);
        }
        return false;
    }

    @Override
    public boolean create(User entity) throws DaoException {
        ResultSet keys = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getName());
            statement.setString(4, entity.getSurname());
            statement.setString(5, entity.getRole().name());
            statement.setString(6, entity.getStatus().name());
            statement.executeUpdate();
            keys = statement.getGeneratedKeys(); //todo where to close resultSet?
            if (keys.next()) {
                int id = keys.getInt(1);
                entity.setId(id);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            close(keys);
        }
        return true; //fixme
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
            int result = statement.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }
}
