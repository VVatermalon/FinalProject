package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.SizeDao;
import by.skarulskaya.finalproject.model.dao.impl.CategoryDaoImpl;
import by.skarulskaya.finalproject.model.dao.impl.SizeDaoImpl;
import by.skarulskaya.finalproject.model.entity.ItemCategory;
import by.skarulskaya.finalproject.model.entity.ItemSize;
import by.skarulskaya.finalproject.model.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ItemSizeService {
    private static final Logger logger = LogManager.getLogger();
    private static final ItemSizeService INSTANCE = new ItemSizeService();
    private ItemSizeService(){}
    public static ItemSizeService getInstance() {
        return INSTANCE;
    }

    public Optional<ItemSize> findSizeById(int id) throws ServiceException {
        SizeDao sizeDao = new SizeDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(sizeDao);
            return sizeDao.findEntityById(id);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    public List<ItemSize> findAll() throws ServiceException {
        SizeDao sizeDao = new SizeDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(sizeDao);
            return sizeDao.findAll();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }
}
