package by.skarulskaya.finalproject.validator;

import java.util.Map;

public interface BaseValidator {
    boolean validateEmail(String email);
    boolean validatePassword(String password);
    boolean validateName(String name);
    boolean validatePhoneNumber(String phoneNumber);
    boolean validateRegistration(Map<String, String> map);
}
