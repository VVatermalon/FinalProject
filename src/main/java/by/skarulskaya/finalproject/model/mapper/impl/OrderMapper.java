package by.skarulskaya.finalproject.model.mapper.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.Order;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class OrderMapper implements EntityMapper<Order> {
    private static final String ID_LABEL = "order_id";
    private static final String STATUS_LABEL = "status";
    private static final String DATE_LABEL = "date_ordered";
    private static final String CARD_LABEL = "gift_card";
    private static final String PRICE_LABEL = "total_price";

    public Order map(ResultSet resultSet) throws DaoException {
        Order order;
        try {
            int id = resultSet.getInt(ID_LABEL);
            String statusStr = resultSet.getString(STATUS_LABEL).trim();
            Order.OrderStatus status = Order.OrderStatus.valueOf(statusStr);
            Date date = resultSet.getDate(DATE_LABEL);
            String giftCard = resultSet.getString(CARD_LABEL);
            BigDecimal totalPrice = resultSet.getBigDecimal(PRICE_LABEL);
            order = new Order(id, status, date, giftCard, totalPrice);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return order;
    }
}