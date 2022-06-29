package by.skarulskaya.finalproject.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order extends CustomEntity implements Comparable<Order> {
    @Override
    public int compareTo(Order o) {
        if(o == null) {
            return -1;
        }
        int dateComparing = this.getDateOrdered().compareTo(o.getDateOrdered());
        if (dateComparing == 0) {
            return -Integer.compare(this.getId(), o.getId());
        }
        return -dateComparing;
    }

    public enum OrderStatus {
        IN_PROCESS, NEED_CONFIRMATION, CONFIRMED, CANCELLED;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
    private OrderStatus status;
    private Customer customer;
    private LocalDate dateOrdered;
    private Address address;
    private String giftCard;
    private BigDecimal totalPrice;
    private List<OrderComponent> components;

    public Order(OrderStatus status, LocalDate dateOrdered, Address address, String giftCard, BigDecimal totalPrice, List<OrderComponent> components) {
        this.status = status;
        this.dateOrdered = dateOrdered;
        this.address = address;
        this.giftCard = giftCard;
        this.totalPrice = totalPrice;
        this.components = components;
    }

    public Order(int id, OrderStatus status, LocalDate dateOrdered, Address address, String giftCard, BigDecimal totalPrice, List<OrderComponent> components) {
        super(id);
        this.status = status;
        this.dateOrdered = dateOrdered;
        this.address = address;
        this.giftCard = giftCard;
        this.totalPrice = totalPrice;
        this.components = components;
    }

    public Order(int id, OrderStatus status, LocalDate dateOrdered, String giftCard, BigDecimal totalPrice) {
        super(id);
        this.status = status;
        this.dateOrdered = dateOrdered;
        this.giftCard = giftCard;
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(LocalDate dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getGiftCard() {
        return giftCard;
    }

    public void setGiftCard(String giftCard) {
        this.giftCard = giftCard;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderComponent> getComponents() {
        return components;
    }

    public void setComponents(List<OrderComponent> components) {
        this.components = components;
    }

    //todo tostring, hashcode, equals
}
