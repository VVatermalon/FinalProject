package by.skarulskaya.finalproject.controller.filter.access;

import java.util.Set;

import static by.skarulskaya.finalproject.controller.PagesPaths.*;

public enum PageAccess {
    ADMIN(Set.of(SIGN_IN_PAGE,
            CART_PAGE,
            BANK_ACCOUNT_PAGE,
            ADD_SHIPPING_ADDRESS_PAGE,
            PAYMENT_PAGE,
            ORDER_SUCCESS_PAGE,
            ORDER_HISTORY_PAGE)),
    CUSTOMER(Set.of(SIGN_IN_PAGE,
            REGISTRATION_PAGE,
            ORDERS_PAGE,
            USERS_PAGE,
            SIZES_PAGE,
            CATEGORIES_PAGE)),
    GUEST(Set.of(CART_PAGE,
            BANK_ACCOUNT_PAGE,
            ADD_SHIPPING_ADDRESS_PAGE,
            PAYMENT_PAGE,
            ORDER_SUCCESS_PAGE,
            SETTINGS_PAGE,
            ORDER_HISTORY_PAGE,
            ORDERS_PAGE,
            USERS_PAGE,
            SIZES_PAGE,
            CATEGORIES_PAGE));

    Set<String> userPages;

    PageAccess(Set<String> userPages) {
        this.userPages = userPages;
    }

    public Set<String> getPages() {
        return userPages;
    }
}
