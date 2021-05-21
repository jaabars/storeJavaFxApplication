package sample.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Price {
    private Long id;
    private Double price;
    private Product product;
    private LocalDate startDate;
    private LocalDate endDate;

    public Price(Long id, Double price, Product product, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.price = price;
        this.product = product;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Price(Double price, LocalDate startDate, LocalDate endDate) {
        this.price = price;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
