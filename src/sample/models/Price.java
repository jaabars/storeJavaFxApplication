package sample.models;

import java.time.LocalDateTime;

public class Price {
    private Long id;
    private double price;
    private Product product;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Price(Long id, double price, Product product, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.price = price;
        this.product = product;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Price() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
