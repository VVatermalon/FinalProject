package by.skarulskaya.finalproject.validator;

import java.util.Map;

public interface BaseValidator {
    boolean validateEmail(String email);
    boolean validatePassword(String password);
    boolean validateName(String name);
    boolean validatePhoneNumber(String phoneNumber);
    boolean validateRegistration(Map<String, String> map);
    boolean validateSignIn(String email, String password);
    boolean validateCountry(String country);
    boolean validateCity(String city);
    boolean validateAddress(String address);
    boolean validateApartment(String apartment);
    boolean validatePostalCode(String postalCode);
    boolean validateAddress(Map<String, String> map);
}
