package by.skarulskaya.finalproject.model.pool;

import by.skarulskaya.finalproject.exception.ConnectionPoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class CustomConnectionPool {
    private static final Logger logger = LogManager.getLogger();
    private static final String DATABASE_PROPERTIES_PATH = "dataSrc.database";
    private static final String POOL_SIZE_PROPERTY_NAME = "pool.size";
    private static final long ONE_HOUR = 60*(long)60*1000;
    private static final int DEFAULT_POOL_SIZE;
    private static final AtomicBoolean create = new AtomicBoolean(false);
    private static final ReentrantLock lockerCreator = new ReentrantLock();
    private static CustomConnectionPool instance;
    private final BlockingQueue<ProxyConnection> freeConnections;
    private final Queue<Connection> givenAwayConnections;
    private final Timer checkConnectionsTimer = new Timer(true);


    static {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(DATABASE_PROPERTIES_PATH);
            DEFAULT_POOL_SIZE = Integer.parseInt(bundle.getString(POOL_SIZE_PROPERTY_NAME));
        } catch(MissingResourceException | NumberFormatException e) {
            logger.fatal(e);
            throw new RuntimeException("Error during properties reading", e);
        }
    }

    private CustomConnectionPool() {
        freeConnections = new LinkedBlockingDeque<>(DEFAULT_POOL_SIZE);
        givenAwayConnections = new ArrayDeque<>(DEFAULT_POOL_SIZE);

        boolean attemptFlag = false;
        while (freeConnections.size() != DEFAULT_POOL_SIZE) {
            try {
                Connection connection = ConnectionFactory.create();
                ProxyConnection proxyConnection = new ProxyConnection(connection);
                freeConnections.add(proxyConnection);
            } catch (ConnectionPoolException e) {
                if (attemptFlag && freeConnections.isEmpty()) {
                    logger.fatal("Error during connection establishing", e);
                    throw new RuntimeException("Error during connection establishing", e);
                }
                attemptFlag = true;
            }
        }
        createTimerTask();
    }

    public static CustomConnectionPool getInstance(){
        if(!create.get()){
            try{
                lockerCreator.lock();
                if(instance == null){
                    instance = new CustomConnectionPool();
                    create.set(true);
                }
            }finally {
                lockerCreator.unlock();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = freeConnections.take();
            givenAwayConnections.offer(connection);
        } catch (InterruptedException e) {
            logger.error(e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public void releaseConnection(Connection connection) throws ConnectionPoolException {
        if (connection.getClass() != ProxyConnection.class) {
            logger.error("Connection isn't instance of ProxyConnection");
            throw new ConnectionPoolException("Connection isn't instance of ProxyConnection");
        }
        givenAwayConnections.remove(connection);
        try {
            freeConnections.put((ProxyConnection) connection);
        } catch (InterruptedException e) {
            logger.error(e);
            Thread.currentThread().interrupt();
        }
    }

    public void destroyPool() {
        checkConnectionsTimer.cancel();
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            try {
                freeConnections.take().closeConnection();
            } catch (InterruptedException e) {
                logger.error(e);
                Thread.currentThread().interrupt();
            }
        }
        deregisterDrivers();
    }

    private void deregisterDrivers() {
        while(DriverManager.getDrivers().hasMoreElements()) {
            Driver driver = DriverManager.getDrivers().nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    private void createTimerTask() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                logger.info("Timer task is checking pool connections");
                try {
                    Iterator<ProxyConnection> iterator = freeConnections.iterator();
                    while(iterator.hasNext()) {
                        ProxyConnection connection = iterator.next();
                        if (!connection.isValid(2)) {
                            connection.closeConnection();
                            iterator.remove();
                            Connection newConnection = ConnectionFactory.create();
                            ProxyConnection proxyConnection = new ProxyConnection(newConnection);
                            freeConnections.add(proxyConnection);
                            logger.info("Timer task replaced connection");
                        }
                    }
                } catch (SQLException | ConnectionPoolException e) {
                    logger.fatal("Error during checking connections timer task working", e);
                    throw new RuntimeException("Error during checking connections timer task working", e);
                }
            }
        };
        checkConnectionsTimer.scheduleAtFixedRate(timerTask, ONE_HOUR, ONE_HOUR);
    }
}
