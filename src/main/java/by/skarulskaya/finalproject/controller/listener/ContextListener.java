package by.skarulskaya.finalproject.controller.listener;

import by.skarulskaya.finalproject.model.pool.CustomConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        CustomConnectionPool.getInstance().destroyPool();
        LogManager.getLogger().info("ContextListener destroyed ConnectionPool");
    }
}