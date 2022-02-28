package by.skarulskaya.finalproject.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order extends CustomEntity {
    public enum OrderStatus {
        IN_PROCESS, NEED_CONFIRMATION, CONFIRMED
    }
    private OrderStatus status;
    private Date dateOrdered;
    private Address address;
    private String giftCard;
    private List<OrderComponent> components;

    public Order() {
        status = OrderStatus.IN_PROCESS;
        components = new ArrayList<>();
    }

    public Order(OrderStatus status, Date dateOrdered, Address address, String giftCard, List<OrderComponent> components) {
        this.status = status;
        this.dateOrdered = dateOrdered;
        this.address = address;
        this.giftCard = giftCard;
        this.components = components;
    }

    public Order(int id, OrderStatus status, Date dateOrdered, Address address, String giftCard, List<OrderComponent> components) {
        super(id);
        this.status = status;
        this.dateOrdered = dateOrdered;
        this.address = address;
        this.giftCard = giftCard;
        this.components = components;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(Date dateOrdered) {
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

    public List<OrderComponent> getComponents() {
        return components;
    }

    public void setComponents(List<OrderComponent> components) {
        this.components = components;
    }

    //todo tostring, hashcode, equals
}
