package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.*;
import by.skarulskaya.finalproject.model.dao.impl.AddressDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.CustomerDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.OrderDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.SizeDaoImpl;
import by.skarulskaya.finalproject.model.entity.*;
import by.skarulskaya.finalproject.util.mail.MailSender;
import by.skarulskaya.finalproject.util.pagination.Pagination;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import static by.skarulskaya.finalproject.controller.Parameters.ORDER_STATUS_ANY;

public class OrderService {
    private static final Logger logger = LogManager.getLogger();
    private static final OrderService INSTANCE = new OrderService();

    private OrderService() {
    }

    public static OrderService getInstance() {
        return INSTANCE;
    }

    public List<Order> findAllCustomerRegisteredOrders(int customerId) throws ServiceException {
        OrderDao orderDao = new OrderDaoImpl();
        OrderComponentService orderComponentService = OrderComponentService.getInstance();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderDao);
            List<Order> orders = orderDao.findAllCustomerRegisteredOrders(customerId);
            for (Order order : orders) {
                List<OrderComponent> orderComponents = orderComponentService.findAllOrderComponents(order.getId());
                orderComponents.sort(null);
                order.setComponents(orderComponents);
            }
            return orders;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Order> findAllRegisteredOrders() throws ServiceException {
        OrderDao orderDao = new OrderDaoImpl();
        OrderComponentService orderComponentService = OrderComponentService.getInstance();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderDao);
            List<Order> orders = orderDao.findAll();
            for (Order order : orders) {
                List<OrderComponent> orderComponents = orderComponentService.findAllOrderComponents(order.getId());
                orderComponents.sort(null);
                order.setComponents(orderComponents);
            }
            return orders;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Order> findAllRegisteredOrdersByPage(int count, int offset) throws ServiceException {
        OrderDao orderDao = new OrderDaoImpl();
        OrderComponentService orderComponentService = OrderComponentService.getInstance();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderDao);
            List<Order> orders = orderDao.findAllByPage(count, offset);
            for (Order order : orders) {
                List<OrderComponent> orderComponents = orderComponentService.findAllOrderComponents(order.getId());
                orderComponents.sort(null);
                order.setComponents(orderComponents);
            }
            return orders;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Order> findAllRegisteredOrdersByDateByStatusByPage(String date, String status, int count, int offset) throws ServiceException {
        OrderDao orderDao = new OrderDaoImpl();
        OrderComponentService orderComponentService = OrderComponentService.getInstance();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderDao);
            List<Order> orders;
            if (status == null || status.equals(ORDER_STATUS_ANY)) {
                if(date == null || date.isEmpty()) {
                    orders = orderDao.findAllByPage(count, offset);
                }
                else {
                    LocalDate orderDate = LocalDate.parse(date);
                    orders = orderDao.findAllByDateByPage(orderDate, count, offset);
                }
            }
            else {
                Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
                if(date == null || date.isEmpty()) {
                    orders = orderDao.findAllByStatusByPage(orderStatus, count, offset);
                }
                else {
                    LocalDate orderDate = LocalDate.parse(date);
                    orders = orderDao.findAllByStatusByDateByPage(orderStatus, orderDate, count, offset);
                }
            }
            for (Order order : orders) {
                List<OrderComponent> orderComponents = orderComponentService.findAllOrderComponents(order.getId());
                orderComponents.sort(null);
                order.setComponents(orderComponents);
            }
            return orders;
        } catch (DaoException | DateTimeParseException | IllegalArgumentException e) {
            throw new ServiceException(e);
        }
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
                throw new ServiceException("Can't update customer bank account, customer id = " + customerId);
            }
            if(!sizeDao.changeAmountInStockForAllOrderItems(orderId)) {
                transaction.rollback();
                return false;
            }
            if(!orderDao.registerOrder(orderId, addressId, cartTotalPrice)) {
                transaction.rollback();
                throw new ServiceException("Can't register order, order id = " + orderId);
            }
            transaction.commit();
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean confirmOrder(int orderId) throws ServiceException {
        OrderDao orderDao = new OrderDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderDao);
            if(orderDao.changeOrderStatus(orderId, Order.OrderStatus.CONFIRMED)) {
                String email = orderDao.findOrderCustomerEmail(orderId);
                sendMessage(email, "Order No " + orderId + " has been confirmed", "Congratulations! Your order has been confirmed by the Administrator and now it is sent to you.");
                return true;
            }
            return false;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean cancelOrder(int orderId) throws ServiceException {
        OrderDao orderDao = new OrderDaoImpl();
        SizeDao sizeDao = new SizeDaoImpl();
        CustomerDao customerDao = new CustomerDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.beginTransaction(customerDao, sizeDao, orderDao);
            if(!orderDao.addMoneyToOrderCustomerAccount(orderId)) {
                transaction.rollback();
                return false;
            }
            if(!sizeDao.changeBackAmountInStockForAllOrderItems(orderId)) {
                transaction.rollback();
                return false;
            }
            if(!orderDao.changeOrderStatus(orderId, Order.OrderStatus.CANCELLED)) {
                transaction.rollback();
                return false;
            }
            transaction.commit();
            String email = orderDao.findOrderCustomerEmail(orderId);
            sendMessage(email, "Order No " + orderId + " has been cancelled", "Your order has been cancelled by the Administrator.");
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private void sendMessage(String email, String mailSubject, String mailText) throws ServiceException {
        try {
            MailSender.INSTANCE.send(email, mailSubject, mailText); //todo сделать тут локализацию
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
