package by.skarulskaya.finalproject.model.dao;

import by.skarulskaya.finalproject.exception.ConnectionPoolException;
import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.pool.CustomConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class EntityTransaction implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger();
    private AbstractDao[] daos;
    private Connection connection;

    public EntityTransaction() {
        connection = CustomConnectionPool.getInstance().getConnection();
    }

    public void beginTransaction(AbstractDao... daos) throws DaoException {
        if(daos == null || daos.length == 0) {
            logger.error("Null parameter");
            throw new DaoException("Null parameter");
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
        for(AbstractDao dao: daos) {
            dao.setConnection(connection);
        }
        this.daos = daos;
    }

    public void init(AbstractDao dao) throws DaoException {
        if (dao == null) {
            logger.error("Null parameter");
            throw new DaoException("Null parameter");
        }
        dao.setConnection(connection);
        daos = new AbstractDao[]{dao};
    }

    @Override
    public void close() throws DaoException {
        if(connection == null) {
            logger.error("Connection value is null");
            throw new DaoException("Connection value is null");
        }
        try {
            if(!connection.getAutoCommit()) {
                rollback();
                connection.setAutoCommit(true);
            }
            CustomConnectionPool.getInstance().releaseConnection(connection);
        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e);
            throw new DaoException(e);
        }
        connection = null;
        for(AbstractDao dao: daos) {
            dao.setConnection(null);
        }
    }
    public void commit() throws DaoException {
        if(connection == null) {
            logger.error("Connection value is null");
            throw new DaoException("Connection value is null");
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }
    public void rollback() throws DaoException {
        if(connection == null) {
            logger.error("Connection value is null");
            throw new DaoException("Connection value is null");
        }
        try {
            connection.rollback();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }
}
