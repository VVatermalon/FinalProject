package by.skarulskaya.finalproject.model.entity;

public class ItemSize extends CustomEntity{
    private String size_name;
    private int amount_in_stock;

    public ItemSize(String size_name, int amount_in_stock) {
        this.size_name = size_name;
        this.amount_in_stock = amount_in_stock;
    }

    public ItemSize(int id, String size_name, int amount_in_stock) {
        super(id);
        this.size_name = size_name;
        this.amount_in_stock = amount_in_stock;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public int getAmount_in_stock() {
        return amount_in_stock;
    }

    public void setAmount_in_stock(int amount_in_stock) {
        this.amount_in_stock = amount_in_stock;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ItemSize{");
        sb.append("id=").append(id);
        sb.append(", size_name='").append(size_name).append('\'');
        sb.append(", count=").append(amount_in_stock);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null) return false;
        if (o.getClass() != getClass()) return false;

        ItemSize itemSize = (ItemSize) o;

        if (amount_in_stock != itemSize.amount_in_stock) return false;
        return size_name.equals(itemSize.size_name);
    }

    @Override
    public int hashCode() {
        int result = size_name.hashCode();
        result = 31 * result + amount_in_stock;
        return result;
    }
}
