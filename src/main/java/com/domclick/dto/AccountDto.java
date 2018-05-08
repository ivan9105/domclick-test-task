package com.domclick.dto;

import com.domclick.model.Account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountDto implements Serializable {
    private Long id;
    private BigDecimal balance;
    private List<LinkDto> links = new ArrayList<>();

    public AccountDto(Account account) {
        this.id = account.getId();
        this.balance = account.getBalance();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<LinkDto> getLinks() {
        return links;
    }

    public void setLinks(List<LinkDto> links) {
        this.links = links;
    }
}
