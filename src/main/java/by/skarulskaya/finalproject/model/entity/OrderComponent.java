package by.skarulskaya.finalproject.model.entity;

import org.apache.logging.log4j.LogManager;

public class OrderComponent extends CustomEntity implements Comparable<OrderComponent> {
    @Override
    public int compareTo(OrderComponent o) {
        if(o == null) {
            return -1;
        }
        int idComparing = Integer.compare(this.getItem().getId(), o.getItem().getId());
        if(idComparing == 0) {
            return Integer.compare(this.getItemSize().getId(), o.getItemSize().getId());
        }
        return idComparing;
    }

    public static class OrderComponentKey {
        private int orderId;
        private int itemId;
        private int itemSizeId;

        public OrderComponentKey(int orderId, int itemId, int itemSizeId) {
            this.orderId = orderId;
            this.itemId = itemId;
            this.itemSizeId = itemSizeId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public int getItemSizeId() {
            return itemSizeId;
        }

        public void setItemSizeId(int itemSizeId) {
            this.itemSizeId = itemSizeId;
        }

        @Override
        public String toString() {
            return "OrderComponentKey{" +
                    "orderId=" + orderId +
                    ", itemId=" + itemId +
                    ", itemSizeId=" + itemSizeId +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o.getClass() != getClass()) return false;

            OrderComponentKey that = (OrderComponentKey) o;

            if (orderId != that.orderId) return false;
            if (itemId != that.itemId) return false;
            return itemSizeId == that.itemSizeId;
        }

        @Override
        public int hashCode() {
            int result = orderId;
            result = 31 * result + itemId;
            result = 31 * result + itemSizeId;
            return result;
        }
    }
    private Item item;
    private int amount;
    private ItemSize itemSize;

    public OrderComponent(Item item, int amount, ItemSize itemSize) {
        this.item = item;
        this.amount = amount;
        this.itemSize = itemSize;
    }

    public OrderComponent(int id, Item item, int amount, ItemSize itemSize) {
        super(id);
        this.item = item;
        this.amount = amount;
        this.itemSize = itemSize;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemSize getItemSize() {
        return itemSize;
    }

    public void setItemSize(ItemSize itemSize) {
        this.itemSize = itemSize;
    }

    @Override
    public String toString() {
        return "OrderComponent{" +
                "id=" + id +
                ", item=" + item +
                ", amount=" + amount +
                ", itemSize=" + itemSize +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != getClass()) return false;

        OrderComponent component = (OrderComponent) o;

        if (amount != component.amount) return false;
        if (item != null ? !item.equals(component.item) : component.item != null) return false;
        return itemSize != null ? itemSize.equals(component.itemSize) : component.itemSize == null;
    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + amount;
        result = 31 * result + (itemSize != null ? itemSize.hashCode() : 0);
        return result;
    }
}
