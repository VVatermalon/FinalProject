package by.skarulskaya.finalproject.model.entity;

import java.math.BigDecimal;

public class Customer extends CustomEntity {
    private BigDecimal bankAccount;
    private String phoneNumber;
    private User user;
    private Address defaultAddress;
}
