package by.skarulskaya.finalproject.model.service.impl;

import by.skarulskaya.finalproject.exception.DaoException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.dao.EntityTransaction;
import by.skarulskaya.finalproject.model.dao.SizeDao;
import by.skarulskaya.finalproject.model.dao.impl.SizeDaoImpl;
import by.skarulskaya.finalproject.model.entity.ItemSize;
import by.skarulskaya.finalproject.model.service.ItemSizeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ItemSizeServiceImpl implements ItemSizeService {
    private static final Logger logger = LogManager.getLogger();
    private static final ItemSizeServiceImpl INSTANCE = new ItemSizeServiceImpl();
    private ItemSizeServiceImpl(){}
    public static ItemSizeServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
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

    @Override
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

    @Override
    public boolean create(String sizeName) throws ServiceException {
        SizeDao sizeDao = new SizeDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(sizeDao);
            ItemSize size = new ItemSize(sizeName, 0);
            return sizeDao.create(size);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean update(ItemSize size) throws ServiceException {
        SizeDao sizeDao = new SizeDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(sizeDao);
            return sizeDao.update(size);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(int sizeId) throws ServiceException {
        SizeDao sizeDao = new SizeDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(sizeDao);
            return sizeDao.delete(sizeId);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean isSizeNameUnique(String sizeName) throws ServiceException {
        SizeDao sizeDao = new SizeDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(sizeDao);
            return sizeDao.isSizeNameUnique(sizeName);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean isSizeNameUnique(String sizeName, int sizeId) throws ServiceException {
        SizeDao sizeDao = new SizeDaoImpl();
        try(EntityTransaction transaction = new EntityTransaction()) {
            transaction.init(sizeDao);
            return sizeDao.isSizeNameUnique(sizeName, sizeId);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }
}
