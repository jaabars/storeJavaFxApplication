package sample.models.dto;

import sample.models.Account;
import sample.models.User;

import java.time.LocalDate;
import java.util.Date;

public class OperationDto {

    private Long id;
    private LocalDate addDate;
    private Account account;
    private double sum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAddDate() {
        return addDate;
    }

    public void setAddDate(LocalDate addDate) {
        this.addDate = addDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
