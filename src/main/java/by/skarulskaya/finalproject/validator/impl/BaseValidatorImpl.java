package by.skarulskaya.finalproject.validator.impl;

import by.skarulskaya.finalproject.model.entity.Address;
import by.skarulskaya.finalproject.validator.BaseValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static by.skarulskaya.finalproject.controller.Parameters.*;

public enum BaseValidatorImpl implements BaseValidator {
    INSTANCE;
    private static final Logger logger = LogManager.getLogger();
    private static final String USER_NAME_PATTERN = "^[A-Za-zА-Яа-я]{3,50}$";
    private static final String USER_PASSWORD_PATTERN = "^[A-Za-zА-Яа-я0-9._*]{5,40}$";
    private static final String USER_EMAIL_PATTERN = "^[a-z0-9._]{1,25}@[a-z]{2,7}\\.[a-z]{2,4}$";
    private static final String USER_PHONE_NUMBER_PATTERN = "\\+375(29|25|44|33)\\d{7}";
    private static final String ADDRESS_CITY_PATTERN = "^[A-Za-zА-Яа-я\\-]{2,20}$";
    private static final String ADDRESS_ADDRESS_PATTERN = "^[\\wА-Яа-я\\h\\.,/]{5,50}$";
    private static final String ADDRESS_APARTMENT_PATTERN = "^(\\d{1,5}|\\d{1,4}[A-Za-zА-Яа-я])$";
    private static final String ADDRESS_POSTAL_CODE_PATTERN = "^[\\dA-Za-z]{3,10}$";
    private static final Double MONEY_MIN_VALUE = 0.01;
    private static final Double MONEY_MAX_VALUE = 999.99;
    private static final int ID_MIN_VALUE = 1;

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
        boolean result = validateRegistrationAdmin(map);
        String phoneNumber = map.get(USER_PHONE_NUMBER);
        if(!validatePhoneNumber(phoneNumber)){
            map.put(USER_PHONE_NUMBER,INVALID_PHONE_NUMBER);
            logger.debug(phoneNumber, USER_PHONE_NUMBER_PATTERN);
            result = false;
        }
        return result;
    }

    @Override
    public boolean validateRegistrationAdmin(Map<String, String> map) {
        boolean result = true;
        String email = map.get(USER_EMAIL);
        String password = map.get(USER_PASSWORD);
        String name = map.get(USER_NAME);
        String surname = map.get(USER_SURNAME);
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
        return result;
    }

    @Override
    public boolean validateSignIn(String email, String password) {
        return validateEmail(email) && validatePassword(password);
    }

    @Override
    public boolean validateCountry(String country) {
        if(country == null || country.isBlank()) {
            return false;
        }
        try {
            Address.AvailableCountries.valueOf(country.toUpperCase());
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean validateCity(String city) {
        return city != null && !city.isBlank() && city.matches(ADDRESS_CITY_PATTERN);
    }

    @Override
    public boolean validateAddress(String address) {
        return address != null && !address.isBlank() && address.matches(ADDRESS_ADDRESS_PATTERN);
    }

    @Override
    public boolean validateApartment(String apartment) {
        return apartment == null || apartment.isBlank() || apartment.matches(ADDRESS_APARTMENT_PATTERN);
    }

    @Override
    public boolean validatePostalCode(String postalCode) {
        return postalCode != null && !postalCode.isBlank() && postalCode.matches(ADDRESS_POSTAL_CODE_PATTERN);
    }

    @Override
    public boolean validateAddress(Map<String, String> map) {
        boolean result = true;
        String country = map.get(COUNTRY);
        String city = map.get(CITY);
        String address = map.get(ADDRESS);
        String apartment = map.get(APARTMENT);
        String postalCode = map.get(POSTAL_CODE);
        if(!validateCountry(country)){
            map.put(COUNTRY, INVALID_COUNTRY);
            result = false;
        }
        if(!validateCity(city)){
            map.put(CITY,INVALID_CITY);
            result = false;
        }
        if(!validateAddress(address)){
            map.put(ADDRESS,INVALID_ADDRESS);
            result = false;
        }
        if(!validateApartment(apartment)){
            map.put(APARTMENT,INVALID_APARTMENT);
            result = false;
        }
        if(!validatePostalCode(postalCode)){
            map.put(POSTAL_CODE,INVALID_POSTAL_CODE);
            result = false;
        }
        return result;
    }

    @Override
    public boolean validatePage(String page) {
        if(page == null) {
            return false;
        }
        try {
            return Integer.parseInt(page) >= 1;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean validateMoney(String money) {
        if(money == null) {
            return false;
        }
        try {
            return Double.parseDouble(money) >= MONEY_MIN_VALUE && Double.parseDouble(money) <= MONEY_MAX_VALUE;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean validateAddItemToCart(Map<String, String> mapData) {
        String itemIdParameter = mapData.get(ITEM_ID);
        if(!validateIntParameter(itemIdParameter)) {
            return false;
        }
        String amountParameter = mapData.get(AMOUNT);
        if(!validateIntParameter(amountParameter)) {
            return false;
        }
        String sizeParameter = mapData.get(SIZE_ID);
        if(sizeParameter != null) {
            return validateIntParameter(sizeParameter);
        }
        return true;
    }

    @Override
    public boolean validateIntParameter(String parameter) {
        if (parameter == null) {
            return false;
        }
        try {
            Integer.parseInt(parameter);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean validateId(String parameter) {
        if (parameter == null) {
            return false;
        }
        try {
            int value = Integer.parseInt(parameter);
            return value >= ID_MIN_VALUE;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
