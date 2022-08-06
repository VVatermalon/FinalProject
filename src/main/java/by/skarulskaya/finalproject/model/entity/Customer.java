package by.skarulskaya.finalproject.model.entity;

import java.math.BigDecimal;
import java.util.Optional;

public class Customer extends User {
    private BigDecimal bankAccount;
    private String phoneNumber;
    private Optional<Address> defaultAddress;

    public Customer(BigDecimal bankAccount, String phoneNumber, Optional<Address> defaultAddress, String email, String password, String name, String surname, User.Role role, User.Status status) {
        super(email, password, name, surname, role, status);
        this.bankAccount = bankAccount;
        this.phoneNumber = phoneNumber;
        this.defaultAddress = defaultAddress;
    }

    public Customer(int id, BigDecimal bankAccount, String phoneNumber, Optional<Address> defaultAddress, String email, String password, String name, String surname, User.Role role, User.Status status) {
        super(id, email, password, name, surname, role, status);
        this.bankAccount = bankAccount;
        this.phoneNumber = phoneNumber;
        this.defaultAddress = defaultAddress;
    }

    public BigDecimal getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BigDecimal bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Optional<Address> getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Optional<Address> defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", bankAccount=" + bankAccount +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", defaultAddress=" + defaultAddress +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != getClass()) return false;
        if (!super.equals(o)) return false;

        Customer customer = (Customer) o;

        if (bankAccount != null ? !bankAccount.equals(customer.bankAccount) : customer.bankAccount != null)
            return false;
        if (phoneNumber != null ? !phoneNumber.equals(customer.phoneNumber) : customer.phoneNumber != null)
            return false;
        return defaultAddress.equals(customer.defaultAddress);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (bankAccount != null ? bankAccount.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + defaultAddress.hashCode();
        return result;
    }
}


