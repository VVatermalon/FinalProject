package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.OrderDao;
import by.skarulskaya.finalproject.model.dao.SizeDao;
import by.skarulskaya.finalproject.model.dao.impl.OrderDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.SizeDaoImpl;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemSize;
import by.skarulskaya.finalproject.model.entity.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class OrderService {
    private static final Logger logger = LogManager.getLogger();
    private static final OrderService INSTANCE = new OrderService();

    private OrderService() {
    }

    public static OrderService getInstance() {
        return INSTANCE;
    }

    public boolean createCart(int customerId, Order order) throws ServiceException {
        OrderDao orderDao = new OrderDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderDao);
            return orderDao.createCartOrder(customerId, order);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public int findCartOrderId(int customerId) throws ServiceException {
        OrderDao orderDao = new OrderDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderDao);
            return orderDao.findCartOrderId(customerId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
