package by.skarulskaya.finalproject.validator.impl;

import by.skarulskaya.finalproject.validator.BaseValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static by.skarulskaya.finalproject.controller.Parameters.*;

public enum BaseValidatorImpl implements BaseValidator {
    INSTANCE;
    private static final String USER_NAME_PATTERN = "^[A-Za-zА-Яа-я]{3,50}$";
    private static final String USER_PASSWORD_PATTERN = "^[A-Za-zА-Яа-я0-9._*]{5,40}$";
    private static final String USER_EMAIL_PATTERN = "^[a-z0-9._]{1,25}@[a-z]{2,7}\\.[a-z]{2,4}$";
    private static final String USER_PHONE_NUMBER_PATTERN = "\\+375(29|25|44|33)\\d{7}";
private static final Logger logger = LogManager.getLogger();

    @Override
    public boolean validateEmail(String email) {
        return email != null && !email.isBlank() && email.toLowerCase().matches(USER_EMAIL_PATTERN);
    }

    @Override
    public boolean validatePassword(String password) {
        return password != null && !password.isBlank() && password.matches(USER_PASSWORD_PATTERN);
    }

    @Override
    public boolean validateName(String name) {
        return name != null && !name.isBlank() && name.matches(USER_NAME_PATTERN);
    }

    @Override
    public boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber != null && !phoneNumber.isBlank() && phoneNumber.matches(USER_PHONE_NUMBER_PATTERN);
    }

    @Override
    public boolean validateRegistration(Map<String, String> map) {
        boolean result = true;
        String email = map.get(USER_EMAIL);
        String password = map.get(USER_PASSWORD);
        String name = map.get(USER_NAME);
        String surname = map.get(USER_SURNAME);
        String phoneNumber = map.get(USER_PHONE_NUMBER);
        if(!validateEmail(email)){
            map.put(USER_EMAIL, INVALID_EMAIL);
            logger.debug(email, USER_EMAIL_PATTERN);
            result = false;
        }
        if(!validatePassword(password)){
            map.put(USER_PASSWORD,INVALID_PASSWORD);
            logger.debug(password, USER_PASSWORD_PATTERN);
            result = false;
        }
        if(!validateName(name)){
            map.put(USER_NAME,INVALID_NAME);
            logger.debug(name, USER_NAME_PATTERN);
            result = false;
        }
        if(!validateName(surname)){
            map.put(USER_SURNAME,INVALID_SURNAME);
            logger.debug(surname, USER_NAME_PATTERN);
            result = false;
        }
        if(!validatePhoneNumber(phoneNumber)){
            map.put(USER_PHONE_NUMBER,INVALID_PHONE_NUMBER);
            logger.debug(phoneNumber, USER_PHONE_NUMBER_PATTERN);
            result = false;
        }
        return result;
    }

    @Override
    public boolean validateSignIn(String email, String password) {
        return validateEmail(email) && validatePassword(password);
    }
}
