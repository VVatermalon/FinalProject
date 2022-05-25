package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.*;
import by.skarulskaya.finalproject.model.dao.impl.AddressDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.CustomerDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.OrderDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.SizeDaoImpl;
import by.skarulskaya.finalproject.model.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderService {
    private static final Logger logger = LogManager.getLogger();
    private static final OrderService INSTANCE = new OrderService();

    private OrderService() {
    }

    public static OrderService getInstance() {
        return INSTANCE;
    }

    public int createCart(int customerId) throws ServiceException {
        OrderDao orderDao = new OrderDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderDao);
            return orderDao.createCartOrder(customerId);
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

    public List<Order> findAllRegisteredOrders(int customerId) throws ServiceException {
        OrderDao orderDao = new OrderDaoImpl();
        AddressDao addressDao = new AddressDaoImpl();
        OrderComponentService orderComponentService = OrderComponentService.getInstance();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(orderDao, addressDao);
            List<Order> orders = orderDao.findAllRegisteredOrders(customerId);
            for (Order order : orders) {
                Optional<Address> addressOpt = addressDao.findAddressByOrderId(order.getId());
                Address address = addressOpt.orElse(null); // todo такое вряд ли может быть, но а если?
                order.setAddress(address);
                List<OrderComponent> orderComponents = orderComponentService.findAllOrderComponents(order.getId());
                order.setComponents(orderComponents);
            }
            transaction.commit();
            return orders;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean registerOrder(Customer customer, int cartOrderId, int addressId, BigDecimal cartTotalPrice) throws ServiceException {
        BigDecimal bankAccount = customer.getBankAccount();
        if(bankAccount.compareTo(cartTotalPrice) < 0) {
            return false;
        }
        BigDecimal newBankAccount = bankAccount.subtract(cartTotalPrice);
        if (registerOder(newBankAccount, customer.getId(), cartOrderId, addressId, cartTotalPrice)) {
            customer.setBankAccount(newBankAccount);
            return true;
        }
        return false;
    }

    private boolean registerOder(BigDecimal newBankAccount, int customerId, int orderId, int addressId, BigDecimal cartTotalPrice) throws ServiceException {
        OrderDao orderDao = new OrderDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        CustomerDao customerDao = new CustomerDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(customerDao, sizeDao, orderDao);
            if(!customerDao.updateBankAccount(newBankAccount, customerId)) {
                transaction.rollback();
                return false;
            }
            if(!sizeDao.changeAmountInStockForAllOrderItems(orderId)) {
                transaction.rollback();
                return false;
            }
            if(!orderDao.registerOrder(orderId, addressId, cartTotalPrice)) {
                transaction.rollback();
                return false;
            }
            transaction.commit();
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
