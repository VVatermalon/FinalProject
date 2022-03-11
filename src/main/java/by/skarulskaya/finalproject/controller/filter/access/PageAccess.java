package by.skarulskaya.finalproject.controller.filter.access;

import java.util.Set;

import static by.skarulskaya.finalproject.controller.PagesPaths.*;

public enum PageAccess {
    ADMIN(Set.of(SIGN_IN_PAGE,
            REGISTRATION_PAGE,
            CART_PAGE,
            BANK_ACCOUNT_PAGE,
            CHECK_OUT_ORDER_PAGE)),
    CUSTOMER(Set.of(SIGN_IN_PAGE,
            REGISTRATION_PAGE,
            USERS_PAGE,
            ADD_MENU_PAGE)),
    GUEST(Set.of(PROFILE_PAGE,
            SETTINGS_PAGE,
            CART_PAGE,
            BANK_ACCOUNT_PAGE,
            CHECK_OUT_ORDER_PAGE));

    Set<String> userPages;

    PageAccess(Set<String> userPages) {
        this.userPages = userPages;
    }

    public Set<String> getPages() {
        return userPages;
    }
}
