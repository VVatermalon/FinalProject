package by.skarulskaya.finalproject.model.entity;

import java.math.BigDecimal;
import java.util.Arrays;

public class Item extends CustomEntity {
    public enum ItemSortParameter {
        ITEM_NAME("name"), PRICE("price"), POPULARITY("popularity");
        private final String name;

        ItemSortParameter(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    private String name;
    private ItemCategory category;
    private BigDecimal price;
    private int amountInStock;
    private double popularity;
    private String description;
    private String imagePath;

    public Item(String name, ItemCategory category, BigDecimal price, int amountInStock, double popularity,
                String description, String imagePath) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.amountInStock = amountInStock;
        this.popularity = popularity;
        this.description = description;
        this.imagePath = imagePath;
    }

    public Item(int id, String name, ItemCategory category, BigDecimal price, int amountInStock, double popularity,
                String description, String imagePath) {
        super(id);
        this.name = name;
        this.category = category;
        this.price = price;
        this.amountInStock = amountInStock;
        this.popularity = popularity;
        this.description = description;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Item{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", category=").append(category);
        sb.append(", price=").append(price);
        sb.append(", amountInStock=").append(amountInStock);
        sb.append(", popularity=").append(popularity);
        sb.append(", description='").append(description).append('\'');
        sb.append(", imagePath='").append(imagePath).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null) return false;
        if (o.getClass() != getClass()) return false;

        Item item = (Item) o;

        if (amountInStock != item.amountInStock) return false;
        if (Double.compare(item.popularity, popularity) != 0) return false;
        if (!name.equals(item.name)) return false;
        if (!category.equals(item.category)) return false;
        if (!price.equals(item.price)) return false;
        if (!description.equals(item.description)) return false;
        return imagePath.equals(item.imagePath);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + amountInStock;
        temp = Double.doubleToLongBits(popularity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + description.hashCode();
        result = 31 * result + imagePath.hashCode();
        return result;
    }
}
