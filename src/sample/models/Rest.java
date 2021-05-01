package sample.models;

public class Rest {
    private Product product;
    private int amount;
    private int maxAmount;
    private int minAmount;

    public Rest(Product product, int amount, int maxAmount, int minAmount) {
        this.product = product;
        this.amount = amount;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
    }

    public Rest() {
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

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }
}
