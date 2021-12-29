package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.model.entity.CustomEntity;
import by.skarulskaya.finalproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao <K, E extends CustomEntity> {
    protected static final Logger logger = LogManager.getLogger();
    protected Connection connection;
    public abstract List<E> findAll() throws DaoException;
    public abstract Optional<E> findEntityById(K id) throws DaoException;
    public abstract boolean delete(E entity) throws DaoException;
    public abstract boolean delete(K id) throws DaoException;
    public abstract boolean create(E entity) throws DaoException;
    public abstract boolean update(E entity) throws DaoException; //todo return bool or E?

    public void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }
    void setConnection(Connection connection) {
        this.connection = connection;
    }
}
