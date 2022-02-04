package by.skarulskaya.finalproject.model.entity;

public class ItemCategory extends CustomEntity{
    private String categoryName;

    public ItemCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public ItemCategory(int id, String categoryName) {
        super(id);
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ItemCategory{");
        sb.append("id=").append(id);
        sb.append(", categoryName='").append(categoryName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null) return false;
        if (o.getClass() != getClass()) return false;

        ItemCategory that = (ItemCategory) o;

        return categoryName.equals(that.categoryName);
    }

    @Override
    public int hashCode() {
        return categoryName.hashCode();
    }
}
