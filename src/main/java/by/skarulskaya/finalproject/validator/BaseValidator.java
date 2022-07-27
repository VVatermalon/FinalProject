package by.skarulskaya.finalproject.validator;

import java.util.HashMap;
import java.util.List;
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

    boolean validateItemName(String name);

    boolean validatePrice(String name);

    boolean validateItemDescription(String name);

    boolean validateItemAmountInStock(List<String> amountsInStock);

    boolean validateItemCategoriesId(List<Integer> categoriesId);

    boolean validateUpdateItem(HashMap<String, String> map, List<Integer> sizesId,
                               List<String> amountsInStock, List<Integer> categoriesId);

    boolean validateCategoryName(String categoryName);

    boolean validateSizeName(String sizeName);
}
