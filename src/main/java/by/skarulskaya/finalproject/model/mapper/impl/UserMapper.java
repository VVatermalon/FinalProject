package by.skarulskaya.finalproject.model.mapper.impl;

import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements EntityMapper<User> {
    private static final String ID_LABEL = "user_id";
    private static final String EMAIL_LABEL = "email";
    private static final String PASSWORD_LABEL = "password";
    private static final String NAME_LABEL = "name";
    private static final String SURNAME_LABEL = "surname";
    private static final String ROLE_LABEL = "role";
    private static final String STATUS_LABEL = "status";

    public User map(ResultSet resultSet) throws DaoException {
        User user;
        try {
            int id = resultSet.getInt(ID_LABEL);
            String email = resultSet.getString(EMAIL_LABEL);
            String password = resultSet.getString(PASSWORD_LABEL);
            String name = resultSet.getString(NAME_LABEL);
            String surname = resultSet.getString(SURNAME_LABEL);
            User.Role role = User.Role.valueOf(resultSet.getString(ROLE_LABEL));
            User.Status status = User.Status.valueOf(resultSet.getString(STATUS_LABEL));
            user = new User(id, email, password, name, surname, role, status);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user;
    }
}
