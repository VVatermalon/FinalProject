package by.skarulskaya.finalproject.model.service;

import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.exception.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<User> signIn(String email, String password) throws ServiceException;
    boolean registerAdmin(Map<String, String> mapData) throws ServiceException;
    List<User> findAllAdminsByPage(int count, int offset) throws ServiceException;
    List<User> findAllAdminsByStatusByPage(String status, int count, int offset) throws ServiceException;
    Optional<User> findUserByEmailAndPassword(String email, String password) throws ServiceException;
    Optional<User> findUserById(int id) throws ServiceException;
    boolean updateName(int userId, String newName) throws ServiceException;
    boolean updateSurname(int userId, String newSurname) throws ServiceException;
    boolean checkCorrectPassword(int userId, String oldPassword) throws ServiceException;
    boolean deleteUser(int userId) throws ServiceException;
    boolean updatePassword(int userId, String newPassword) throws ServiceException;
    boolean changeUserStatus(int userId, User.Status userStatus) throws ServiceException;

}
