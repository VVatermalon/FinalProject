package by.skarulskaya.MusicBandProject.pool;

import by.skarulskaya.MusicBandProject.exception.ConnectionPoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class ConnectionFactory {
    private static final Logger logger = LogManager.getLogger();
    private static final String DATABASE_PROPERTIES_PATH = "dataSrc/database.properties";
    private static final String DRIVER_NAME_PROPERTY_NAME = "db.driver";
    private static final String DATABASE_URL_PROPERTY_NAME = "db.url";
    private static final String DATABASE_URL;
    private static final Properties connectionProperties = new Properties();

    static {
        ClassLoader loader = ConnectionFactory.class.getClassLoader();
        InputStream resourceStream = loader.getResourceAsStream(DATABASE_PROPERTIES_PATH);
        try {
            connectionProperties.load(resourceStream);
            String DRIVER_NAME = connectionProperties.getProperty(DRIVER_NAME_PROPERTY_NAME);
            Class.forName(DRIVER_NAME);
        } catch (IOException | ClassNotFoundException e) {
            logger.fatal("Error during initializing", e);
            throw new RuntimeException(e);
        }
        DATABASE_URL = connectionProperties.getProperty(DATABASE_URL_PROPERTY_NAME);
    }

    private ConnectionFactory() {}

    static Connection create() throws ConnectionPoolException {
        try {
            return DriverManager.getConnection(DATABASE_URL, connectionProperties);
        } catch (SQLException e) {
            logger.error(e);
            throw new ConnectionPoolException(e);
        }
    }
}
