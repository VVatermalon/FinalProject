package by.skarulskaya.finalproject.model.entity;

public class OrderComponent extends CustomEntity {
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
