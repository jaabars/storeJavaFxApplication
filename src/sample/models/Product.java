package sample.models;

public class Product {
    private Long id;
    private String name;
    private String barcode;
    private Category category;
    private boolean active;

    public Product(String name, String barcode, Category category, boolean active) {
        this.name = name;
        this.barcode = barcode;
        this.category = category;
        this.active = active;
    }

    public Product() {
    }

    public Product(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
