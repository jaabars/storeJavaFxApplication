package sample.models;

public class OperationDetail {
    private Long id;
    private Operation operation;
    private Product product;
    private int amount;
    private double price;

    public OperationDetail(Long id, Operation operation, Product product, int amount, double price) {
        this.id = id;
        this.operation = operation;
        this.product = product;
        this.amount = amount;
        this.price = price;
    }

    public OperationDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
