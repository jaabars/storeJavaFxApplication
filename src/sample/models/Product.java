package sample.models;

public class Product {
    private Long id;
    private String name;
    private String barcode;
    private Category category;

    public Product(Long id, String name, String barcode, Category category) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.category = category;
    }
    public Product() {
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
