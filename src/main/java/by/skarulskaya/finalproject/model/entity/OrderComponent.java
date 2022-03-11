package by.skarulskaya.finalproject.model.entity;

public class OrderComponent extends CustomEntity {
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

        //todo generate tostring, hash..
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
    //todo tosstring, equals...
}
