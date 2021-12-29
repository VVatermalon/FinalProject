package by.skarulskaya.finalproject.model.mapper;

import by.skarulskaya.finalproject.model.entity.CustomEntity;
import by.skarulskaya.finalproject.exception.DaoException;

import java.sql.ResultSet;

public interface EntityMapper<E extends CustomEntity> {
    E map(ResultSet resultSet) throws DaoException;
}
