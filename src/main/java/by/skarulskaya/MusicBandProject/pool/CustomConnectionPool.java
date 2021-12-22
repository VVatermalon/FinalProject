package by.skarulskaya.MusicBandProject.pool;

import by.skarulskaya.MusicBandProject.connection.ConnectionCreator;
import by.skarulskaya.MusicBandProject.exception.ConnectionPoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public enum CustomConnectionPool {
    INSTANCE;

    private static final String DATABASE_PROPERTIES_PATH = "dataSrc/database.properties";
    private static final String DRIVER_NAME_PROPERTY_NAME = "db.driver";
    private static final String DATABASE_URL_PROPERTY_NAME = "db.url";
    private final Logger logger = LogManager.getLogger();
    private final BlockingQueue<ProxyConnection> freeConnections;
    private final Queue<Connection> givenAwayConnections;

    private final static int DEFAULT_POOL_SIZE = 1;

    CustomConnectionPool() {
        ClassLoader loader = this.getClass().getClassLoader();
        InputStream resource = loader.getResourceAsStream(DATABASE_PROPERTIES_PATH);
        Properties connectionProperties = new Properties();
        try {
            connectionProperties.load(resource);
            String DRIVER_NAME = connectionProperties.getProperty(DRIVER_NAME_PROPERTY_NAME);
            Class.forName(DRIVER_NAME);
        } catch (IOException | ClassNotFoundException e) {
            logger.fatal("Error during pool initializing", e);
            throw new RuntimeException(e);
        }
        String DATABASE_URL = connectionProperties.getProperty(DATABASE_URL_PROPERTY_NAME);

        freeConnections = new LinkedBlockingDeque<>(DEFAULT_POOL_SIZE);
        givenAwayConnections = new ArrayDeque<>(DEFAULT_POOL_SIZE);

        boolean attemptFlag = false;
        while (freeConnections.size() < DEFAULT_POOL_SIZE) {
            try {
                Connection connection = DriverManager.getConnection(DATABASE_URL, connectionProperties);
                ProxyConnection proxyConnection = new ProxyConnection(connection);
                freeConnections.add(proxyConnection);
            } catch (SQLException e) {
                if (attemptFlag && freeConnections.size() == 0) {
                    logger.fatal("Error during connection establishing");
                    throw new RuntimeException(e);
                }
                attemptFlag = true;
            }
        }
    }

    public Connection getConnection() throws ConnectionPoolException {
        Connection connection = null;
        try {
            connection = freeConnections.take();
        } catch (InterruptedException e) {
            logger.error(e);
            throw new ConnectionPoolException(e);
        }
        givenAwayConnections.offer(connection);
        return connection;
    }

    public void releaseConnection(Connection connection) throws ConnectionPoolException {
        if(!(connection instanceof ProxyConnection)) {
            logger.error("Connection isn't instance of ProxyConnection");
            throw new ConnectionPoolException("Connection isn't instance of ProxyConnection");
        }
        givenAwayConnections.remove(connection);
        freeConnections.offer((ProxyConnection) connection);
    }

    public void destroyPool() throws ConnectionPoolException {
        try {
            for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
                freeConnections.take().closeConnection();
            }
            deregisterDrivers();
        } catch (InterruptedException e) {
            logger.error(e);
            throw new ConnectionPoolException(e);
        }
    }

    private void deregisterDrivers() throws ConnectionPoolException {
        while(DriverManager.getDrivers().hasMoreElements()) {
            Driver driver = DriverManager.getDrivers().nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error(e);
                throw new ConnectionPoolException(e);
            }
        }
    }
}
