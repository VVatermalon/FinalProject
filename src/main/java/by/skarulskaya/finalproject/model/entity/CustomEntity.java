package by.skarulskaya.finalproject.model.entity;

public class CustomEntity {
    protected int id;

    protected CustomEntity() {
    }

    protected CustomEntity(int id) {
        this.id = id;
    }

    public int getId() { return this.id; }

    public void setId(int id) {
        this.id = id;
    }

    //todo tostring
}
