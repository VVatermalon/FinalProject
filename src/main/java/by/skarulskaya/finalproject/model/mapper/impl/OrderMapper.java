package by.skarulskaya.finalproject.model.mapper.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.*;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class OrderMapper implements EntityMapper<Order> {
    private static final String ID_LABEL = "order_id";
    private static final String CUSTOMER_ID_LABEL = "customer_id";
    private static final String STATUS_LABEL = "order_status";
    private static final String DATE_LABEL = "date_ordered";
    private static final String SHIPPING_ADDRESS_ID_LABEL = "shipping_address";
    private static final String CARD_LABEL = "gift_card";
    private static final String PRICE_LABEL = "total_price";

    public Order map(ResultSet resultSet) throws DaoException {
        Order order;
        try {
            int id = resultSet.getInt(ID_LABEL);
            int customerId = resultSet.getInt(CUSTOMER_ID_LABEL);
            String statusStr = resultSet.getString(STATUS_LABEL).trim();
            Order.OrderStatus status = Order.OrderStatus.valueOf(statusStr);
            LocalDate date = resultSet.getTimestamp(DATE_LABEL).toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime().toLocalDate();
            int addressId = resultSet.getInt(SHIPPING_ADDRESS_ID_LABEL);
            String giftCard = resultSet.getString(CARD_LABEL);
            BigDecimal totalPrice = resultSet.getBigDecimal(PRICE_LABEL);
            order = new Order(id, status, date, giftCard, totalPrice);
            if (customerId != 0) {
                EntityMapper<Customer> customerMapper = new CustomerMapper();
                Customer customer = customerMapper.map(resultSet);
                order.setCustomer(customer);
            }
            if (addressId != 0) {
                EntityMapper<Address> addressMapper = new AddressMapper();
                Address address = addressMapper.map(resultSet);
                order.setAddress(address);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return order;
    }
}