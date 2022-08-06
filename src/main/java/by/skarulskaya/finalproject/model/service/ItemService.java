package by.skarulskaya.finalproject.model.service;

import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> findAllByCategoryByPageSort(String categoryParameter, String sortParameter, String sortOrderParameter,
                                           int count, int offset) throws ServiceException;
    Optional<Item> findItemById(int id) throws ServiceException;
    boolean updateOrCreate(HashMap<String, String> map, List<Integer> sizesId, List<String> amountsInStock,
                           List<Integer> categoriesId) throws ServiceException;
    boolean updateItemImagePath(int id, String path) throws ServiceException;
    boolean updatePopularity(int id) throws ServiceException;
    boolean delete(int id) throws ServiceException;
}
