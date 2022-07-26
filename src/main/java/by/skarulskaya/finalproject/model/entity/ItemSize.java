package by.skarulskaya.finalproject.model.entity;

public class ItemSize extends CustomEntity{
    private String sizeName;
    private int amountInStock;

    public ItemSize(String sizeName, int amountInStock) {
        this.sizeName = sizeName;
        this.amountInStock = amountInStock;
    }

    public ItemSize(int id, String sizeName) {
        super(id);
        this.sizeName = sizeName;
    }

    public ItemSize(int id, int amountInStock) {
        super(id);
        this.amountInStock = amountInStock;
    }

    public ItemSize(int id, String sizeName, int amountInStock) {
        super(id);
        this.sizeName = sizeName;
        this.amountInStock = amountInStock;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }

    @Override
    public String toString() {
        return "ItemSize{" +
                "id=" + id +
                ", sizeName='" + sizeName + '\'' +
                ", amountInStock=" + amountInStock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null) return false;
        if (o.getClass() != getClass()) return false;

        ItemSize itemSize = (ItemSize) o;

        if (amountInStock != itemSize.amountInStock) return false;
        return sizeName.equals(itemSize.sizeName);
    }

    @Override
    public int hashCode() {
        int result = sizeName.hashCode();
        result = 31 * result + amountInStock;
        return result;
    }
}
