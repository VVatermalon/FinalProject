package by.skarulskaya.finalproject.model.mapper.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.model.mapper.EntityMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper implements EntityMapper<Item> {
    private static final String ID_LABEL = "item_id";
    private static final String NAME_LABEL = "item_name";
    private static final String PRICE_LABEL = "price";
    private static final String AMOUNT_LABEL = "amount_in_stock";
    private static final String POPULARITY_LABEL = "popularity";
    private static final String IMAGE_LABEL = "image_path";
    private static final String DESCRIPTION_LABEL = "description";

    public Item map(ResultSet resultSet) throws DaoException {
        Item item;
        try {
            int id = resultSet.getInt(ID_LABEL);
            String name = resultSet.getString(NAME_LABEL);
            BigDecimal price = resultSet.getBigDecimal(PRICE_LABEL);
            int amount = resultSet.getInt(AMOUNT_LABEL);
            long popularity = resultSet.getLong(POPULARITY_LABEL);
            String image = resultSet.getString(IMAGE_LABEL);
            String description = resultSet.getString(DESCRIPTION_LABEL);
            item = new Item(id, name, price, amount, popularity, description, image);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return item;
    }
}
