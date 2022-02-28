package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemSize;
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

//    public void checkItemsInCart (HashMap<Map.Entry<Item, ItemSize>, Integer> cart) {
//
//    }
}
