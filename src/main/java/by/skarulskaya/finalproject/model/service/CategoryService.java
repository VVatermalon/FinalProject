package by.skarulskaya.finalproject.model.service;

import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.ItemCategory;

import java.util.List;

public interface CategoryService {
    List<ItemCategory> findAllCategories() throws ServiceException;
    boolean create(String categoryName) throws ServiceException;
    boolean update(ItemCategory category) throws ServiceException;
    boolean delete(int categoryId) throws ServiceException;
    boolean isCategoryNameUnique(String categoryName) throws ServiceException;
    boolean isCategoryNameUnique(String categoryName, int categoryId) throws ServiceException;

}
