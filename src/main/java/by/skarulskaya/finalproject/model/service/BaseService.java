package by.skarulskaya.finalproject.model.service;

import by.skarulskaya.finalproject.model.entity.User;
import by.skarulskaya.finalproject.exception.ServiceException;

import java.util.Map;
import java.util.Optional;

public interface BaseService {
    Optional<User> signIn(String login, String password) throws ServiceException;
    boolean registerUser(Map<String, String> mapData) throws ServiceException;
}
