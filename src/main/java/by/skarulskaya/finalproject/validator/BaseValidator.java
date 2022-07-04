package by.skarulskaya.finalproject.validator;

import java.util.Map;

public interface BaseValidator {
    boolean validateEmail(String email);
    boolean validatePassword(String password);
    boolean validateName(String name);
    boolean validatePhoneNumber(String phoneNumber);
    boolean validateRegistration(Map<String, String> map);

    boolean validateRegistrationAdmin(Map<String, String> map);

    boolean validateSignIn(String email, String password);
    boolean validateCountry(String country);
    boolean validateCity(String city);
    boolean validateAddress(String address);
    boolean validateApartment(String apartment);
    boolean validatePostalCode(String postalCode);
    boolean validateAddress(Map<String, String> map);

    boolean validatePage(String page);

    boolean validateMoney(String money);

    boolean validateAddItemToCart(Map<String, String> mapData);

    boolean validateIntParameter(String parameter);

    boolean validateId(String parameter);
}
