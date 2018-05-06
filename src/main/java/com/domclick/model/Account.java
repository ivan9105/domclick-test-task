package com.domclick.model;

import javax.persistence.*;

@Entity
@Table(name = "BANK_ACCOUNT")
public class Account extends BaseEntity {
    @Column(name = "BALANCE", nullable = false)
    private Double balance;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
