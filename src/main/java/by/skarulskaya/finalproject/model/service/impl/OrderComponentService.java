package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.OrderComponentDao;
import by.skarulskaya.finalproject.model.dao.impl.OrderComponentDaoImpl;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemSize;
import by.skarulskaya.finalproject.model.entity.OrderComponent;
import by.skarulskaya.finalproject.model.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

public class OrderComponentService {
    private static final Logger logger = LogManager.getLogger();
    private static final OrderComponentService INSTANCE = new OrderComponentService();
    private final ItemService itemService = ItemService.getInstance();

    private OrderComponentService() {
    }

    public static OrderComponentService getInstance() {
        return INSTANCE;
    }

    public boolean uploadCart(int cartOrderId, List<OrderComponent> uploadedCart) throws ServiceException {
        OrderComponentDao orderComponentDao = new OrderComponentDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderComponentDao);
            HashMap<OrderComponent.OrderComponentKey, Integer> cart = orderComponentDao.findAllCartComponents(cartOrderId);
            for (OrderComponent.OrderComponentKey key : cart.keySet()) {
                Item item = itemService.findItemById(key.getItemId()).get();
                Optional<ItemSize> sizeOpt = item.getSizes().stream()
                        .filter(s -> s.getId() == key.getItemSizeId())
                        .findFirst();
                if (sizeOpt.isEmpty()) {
                    throw new ServiceException("Can't find item size"); //todo better remove this item from cart
                }
                ItemSize itemSize = sizeOpt.get();
                int amountInStock = itemSize.getAmountInStock();
                if (amountInStock == 0) {
                    cart.remove(key);
                    removeItemFromCart(key);
                    result = true;
                    continue;
                }
                int amountInCart = cart.get(key);
                if (amountInStock < amountInCart) {
                    amountInCart = amountInStock;
                    changeItemAmountInCart(key, amountInCart);
                    result = true;
                }
                OrderComponent component = new OrderComponent(item, amountInCart, itemSize);
                uploadedCart.add(component);
            }
            return result;
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    public int addItemToCart(OrderComponent.OrderComponentKey key, int amount) throws ServiceException {
        if (amount < 1) {
            return 0;
        }
        Item item = itemService.findItemById(key.getItemId()).get();
        Optional<ItemSize> sizeOpt = item.getSizes().stream()
                .filter(s -> s.getId() == key.getItemSizeId())
                .findFirst();
        if (sizeOpt.isEmpty()) {
            throw new ServiceException("Can't find item size"); //todo better remove this item from cart
        }
        ItemSize itemSize = sizeOpt.get();
        int amountInStock = itemSize.getAmountInStock();
        if (amountInStock <= 0) {
            return 0;
        }
        int newAmount = Math.min(50, amount);
        int itemAmountInCart = itemAmountInCart(key);
        if (itemAmountInCart > 0) {
            newAmount = itemAmountInCart + newAmount;
            newAmount = Math.min(amountInStock, newAmount);
            if (!changeAmountInOrderComponent(key, newAmount)) {
                return 0;
            }
        } else {
            newAmount = Math.min(amountInStock, newAmount);
            if (!createOrderComponent(key, newAmount)) {
                return 0;
            }
        }
        return newAmount - itemAmountInCart;
    }

    public boolean removeItemFromCart(OrderComponent.OrderComponentKey key) throws ServiceException {
        OrderComponentDao orderComponentDao = new OrderComponentDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderComponentDao);
            return orderComponentDao.delete(key);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    public boolean changeItemAmountInCart(OrderComponent.OrderComponentKey key, int amount) throws ServiceException {
        if (amount < 1) {
            return false;
        }
        Item item = itemService.findItemById(key.getItemId()).get();
        Optional<ItemSize> sizeOpt = item.getSizes().stream()
                .filter(s -> s.getId() == key.getItemSizeId())
                .findFirst();
        if (sizeOpt.isEmpty()) {
            throw new ServiceException("Can't find item size"); //todo better remove this item from cart
        }
        ItemSize itemSize = sizeOpt.get();
        int amountInStock = itemSize.getAmountInStock();
        if (amountInStock < amount || amount > 50) {
            return false;
        }
        return changeAmountInOrderComponent(key, amount);
    }

    public int countItemsInCart(int cartOrderId) throws ServiceException {
        OrderComponentDao orderComponentDao = new OrderComponentDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderComponentDao);
            return orderComponentDao.countItemsInCart(cartOrderId);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    public BigDecimal findCartTotalPrice(int cartOrderId) throws ServiceException {
        OrderComponentDao orderComponentDao = new OrderComponentDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderComponentDao);
            return orderComponentDao.findCartTotalPrice(cartOrderId);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    public boolean checkCartBeforePayment(int cartOrderId) throws ServiceException {
        OrderComponentDao orderComponentDao = new OrderComponentDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderComponentDao);
            return orderComponentDao.findNotEnoughInStockItemsInOrder(cartOrderId);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    private int itemAmountInCart(OrderComponent.OrderComponentKey key) throws ServiceException {
        OrderComponentDao orderComponentDao = new OrderComponentDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderComponentDao);
            return orderComponentDao.findItemAmountInCart(key);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    private boolean createOrderComponent(OrderComponent.OrderComponentKey key, int amount) throws ServiceException {
        OrderComponentDao orderComponentDao = new OrderComponentDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderComponentDao);
            return orderComponentDao.createByKey(key, amount);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    private boolean changeAmountInOrderComponent(OrderComponent.OrderComponentKey key, int amount) throws ServiceException {
        OrderComponentDao orderComponentDao = new OrderComponentDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderComponentDao);
            return orderComponentDao.changeItemAmountInCart(key, amount);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }
}
