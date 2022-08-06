package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.OrderComponentDao;
import by.skarulskaya.finalproject.model.dao.impl.OrderComponentDaoImpl;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemSize;
import by.skarulskaya.finalproject.model.entity.OrderComponent;
import by.skarulskaya.finalproject.model.service.OrderComponentService;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.ERROR_CANNOT_ADD_MORE_LIMIT;
import static by.skarulskaya.finalproject.controller.ParametersMessages.ERROR_NOT_ENOUGH_ITEMS_IN_STOCK;

public class OrderComponentServiceImpl implements OrderComponentService {
    private static final Logger logger = LogManager.getLogger();
    private static final int ITEM_IN_CART_AMOUNT_LIMIT = 50;
    private static final int DEFAULT_SIZE_ID = 1;
    private static final OrderComponentServiceImpl INSTANCE = new OrderComponentServiceImpl();
    private final ItemServiceImpl itemService = ItemServiceImpl.getInstance();

    private OrderComponentServiceImpl() {
    }

    public static OrderComponentServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean uploadCart(int cartOrderId, List<OrderComponent> uploadedCart) throws ServiceException {
        OrderComponentDao orderComponentDao = new OrderComponentDaoImpl();
        boolean cartWasChanged = false;
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderComponentDao);
            HashMap<OrderComponent.OrderComponentKey, Integer> cart = orderComponentDao.findAllOrderComponents(cartOrderId);
            for (OrderComponent.OrderComponentKey key : cart.keySet()) {
                Item item = itemService.findItemById(key.getItemId()).get();
                ItemSize itemSize = item.getSizes().stream()
                        .filter(s -> s.getId() == key.getItemSizeId())
                        .findFirst()
                        .get();
                int amountInStock = itemSize.getAmountInStock();
                if (amountInStock == 0) {
                    cart.remove(key);
                    removeItemFromCart(key);
                    cartWasChanged = true;
                    continue;
                }
                int amountInCart = cart.get(key);
                if (amountInStock < amountInCart) {
                    amountInCart = amountInStock;
                    changeAmountInOrderComponent(key, amountInCart);
                    cartWasChanged = true;
                }
                OrderComponent component = new OrderComponent(item, amountInCart, itemSize);
                uploadedCart.add(component);
            }
            return cartWasChanged;
        } catch (DaoException | NoSuchElementException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<OrderComponent> findAllOrderComponents(int orderId) throws ServiceException {
        List<OrderComponent> components = new ArrayList<>();
        OrderComponentDao orderComponentDao = new OrderComponentDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderComponentDao);
            HashMap<OrderComponent.OrderComponentKey, Integer> cart = orderComponentDao.findAllOrderComponents(orderId);
            for (OrderComponent.OrderComponentKey key : cart.keySet()) {
                Item item = itemService.findItemById(key.getItemId()).get();
                Optional<ItemSize> sizeOpt = item.getSizes().stream()
                        .filter(s -> s.getId() == key.getItemSizeId())
                        .findFirst();
                ItemSize itemSize = sizeOpt.orElse(null); //todo это маловероятно, но а вдруг?
                int amountInOrder = cart.get(key);
                OrderComponent component = new OrderComponent(item, amountInOrder, itemSize);
                components.add(component);
            }
            return components;
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean addItemToCart(Map<String, String> mapData) throws ServiceException {
        if (!BaseValidatorImpl.getInstance().validateAddItemToCart(mapData)) {
            throw new ServiceException("Invalid parameter");
        }
        int cartOrderId = Integer.parseInt(mapData.get(CART_ORDER_ID));
        int itemId = Integer.parseInt(mapData.get(ITEM_ID));
        int amount = Integer.parseInt(mapData.get(AMOUNT));
        String sizeParameter = mapData.get(SIZE_ID);
        int sizeId = sizeParameter == null ? DEFAULT_SIZE_ID : Integer.parseInt(sizeParameter);
        OrderComponent.OrderComponentKey key = new OrderComponent.OrderComponentKey(cartOrderId, itemId, sizeId);
        if (amount < 1 && (mapData.get(CHANGE_FROM_CART) == null || amount != -1)) {
            throw new ServiceException("Invalid amount = " + amount);
        }
        Optional<Item> itemOptional = itemService.findItemById(itemId);
        if (itemOptional.isEmpty()) {
            mapData.put(ITEM_ID, INVALID_ITEM_ID);
            return false;
        }
        itemService.updatePopularity(itemId);
        Item item = itemOptional.get();
        Optional<ItemSize> sizeOpt = item.getSizes().stream()
                .filter(s -> s.getId() == sizeId)
                .findFirst();
        if (sizeOpt.isEmpty()) {
            mapData.put(SIZE_ID, INVALID_SIZE_ID);
            return false;
        }
        ItemSize itemSize = sizeOpt.get();
        int amountInStock = itemSize.getAmountInStock();
        int itemAmountInCart = itemAmountInCart(key);
        int newAmount = itemAmountInCart + amount;
        boolean errorFlag = true;
        if (newAmount > amountInStock) {
            newAmount = amountInStock;
            mapData.put(AMOUNT, ERROR_NOT_ENOUGH_ITEMS_IN_STOCK);
            errorFlag = false;
        }
        if (newAmount > ITEM_IN_CART_AMOUNT_LIMIT) {
            newAmount = ITEM_IN_CART_AMOUNT_LIMIT;
            mapData.put(AMOUNT, ERROR_CANNOT_ADD_MORE_LIMIT);
            errorFlag = false;
        }
        if (itemAmountInCart > 0) {
            if (!changeAmountInOrderComponent(key, newAmount)) {
                mapData.put(CART_ORDER_ID, ERROR_ADD_ITEM_TO_CART);
                return false;
            }
        } else {
            if (!createOrderComponent(key, newAmount)) {
                mapData.put(CART_ORDER_ID, ERROR_ADD_ITEM_TO_CART);
                return false;
            }
        }
        return errorFlag;
    }

    @Override
    public boolean removeItemFromCart(Map<String, String> mapData) throws ServiceException {
        try {
            int cartOrderId = Integer.parseInt(mapData.get(CART_ORDER_ID));
            String itemIdParameter = mapData.get(ITEM_ID);
            if (!BaseValidatorImpl.getInstance().validateId(itemIdParameter)) {
                throw new ServiceException("Invalid item id " + itemIdParameter);
            }
            int itemId = Integer.parseInt(mapData.get(ITEM_ID));
            String sizeParameter = mapData.get(SIZE_ID);
            int sizeId = sizeParameter == null ? DEFAULT_SIZE_ID : Integer.parseInt(sizeParameter);
            OrderComponent.OrderComponentKey key = new OrderComponent.OrderComponentKey(cartOrderId, itemId, sizeId);
            return removeItemFromCart(key);
        } catch (NumberFormatException e) {
            throw new ServiceException(e);
        }
    }

    @Override
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

    @Override
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

    private boolean removeItemFromCart(OrderComponent.OrderComponentKey key) throws ServiceException {
        OrderComponentDao orderComponentDao = new OrderComponentDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(orderComponentDao);
            return orderComponentDao.delete(key);
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
