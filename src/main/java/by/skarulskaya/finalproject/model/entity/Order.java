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

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", customer=" + customer +
                ", dateOrdered=" + dateOrdered +
                ", address=" + address +
                ", giftCard='" + giftCard + '\'' +
                ", totalPrice=" + totalPrice +
                ", components=" + components +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != getClass()) return false;

        Order order = (Order) o;

        if (status != order.status) return false;
        if (customer != null ? !customer.equals(order.customer) : order.customer != null) return false;
        if (dateOrdered != null ? !dateOrdered.equals(order.dateOrdered) : order.dateOrdered != null) return false;
        if (address != null ? !address.equals(order.address) : order.address != null) return false;
        if (giftCard != null ? !giftCard.equals(order.giftCard) : order.giftCard != null) return false;
        if (totalPrice != null ? !totalPrice.equals(order.totalPrice) : order.totalPrice != null) return false;
        return components != null ? components.equals(order.components) : order.components == null;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (dateOrdered != null ? dateOrdered.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (giftCard != null ? giftCard.hashCode() : 0);
        result = 31 * result + (totalPrice != null ? totalPrice.hashCode() : 0);
        result = 31 * result + (components != null ? components.hashCode() : 0);
        return result;
    }
}
