package com.domclick.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountResponse implements Serializable {
    private List<AccountDto> accounts = new ArrayList<>();

    public List<AccountDto> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountDto> accounts) {
        this.accounts = accounts;
    }
}
