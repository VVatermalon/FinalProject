package by.skarulskaya.finalproject.model.service;

import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.ItemSize;

import java.util.List;
import java.util.Optional;

public interface ItemSizeService {
    Optional<ItemSize> findSizeById(int id) throws ServiceException;
    List<ItemSize> findAll() throws ServiceException;
    boolean create(String sizeName) throws ServiceException;
    boolean update(ItemSize size) throws ServiceException;
    boolean delete(int sizeId) throws ServiceException;
    boolean isSizeNameUnique(String sizeName) throws ServiceException;
    boolean isSizeNameUnique(String sizeName, int sizeId) throws ServiceException;
}
