package by.skarulskaya.finalproject.model.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private List<ItemCategory> categories;
    private List<ItemSize> sizes;
    private BigDecimal price;
    private int amountInStock;
    private long popularity;
    private String description;
    private String imagePath;

    public Item(String name, List<ItemCategory> categories, List<ItemSize> sizes, BigDecimal price,
                int amountInStock, long popularity, String description, String imagePath) {
        this.name = name;
        this.categories = categories;
        this.sizes = sizes;
        this.price = price;
        this.amountInStock = amountInStock;
        this.popularity = popularity;
        this.description = description;
        this.imagePath = imagePath;
    }

    public Item(int id, String name, List<ItemCategory> categories, List<ItemSize> sizes, BigDecimal price,
                int amountInStock, long popularity, String description, String imagePath) {
        super(id);
        this.name = name;
        this.categories = categories;
        this.sizes = sizes;
        this.price = price;
        this.amountInStock = amountInStock;
        this.popularity = popularity;
        this.description = description;
        this.imagePath = imagePath;
    }

    public Item(int id, String name, BigDecimal price, int amountInStock, long popularity,
                String description, String imagePath) {
        super(id);
        this.name = name;
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

    public List<ItemCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ItemCategory> categories) {
        this.categories = categories;
    }

    public List<ItemSize> getSizes() {
        return sizes;
    }

    public void setSizes(List<ItemSize> sizes) {
        this.sizes = sizes;
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

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
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
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categories=" + categories +
                ", sizes=" + sizes +
                ", price=" + price +
                ", amountInStock=" + amountInStock +
                ", popularity=" + popularity +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != getClass()) return false;

        Item item = (Item) o;

        if (amountInStock != item.amountInStock) return false;
        if (popularity != item.popularity) return false;
        if (name != null ? !name.equals(item.name) : item.name != null) return false;
        if (categories != null ? !categories.equals(item.categories) : item.categories != null) return false;
        if (sizes != null ? !sizes.equals(item.sizes) : item.sizes != null) return false;
        if (price != null ? !price.equals(item.price) : item.price != null) return false;
        if (description != null ? !description.equals(item.description) : item.description != null) return false;
        return imagePath != null ? imagePath.equals(item.imagePath) : item.imagePath == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        result = 31 * result + (sizes != null ? sizes.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + amountInStock;
        result = 31 * result + (int) (popularity ^ (popularity >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (imagePath != null ? imagePath.hashCode() : 0);
        return result;
    }
}
