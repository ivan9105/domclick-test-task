package com.domclick.dto;

import com.domclick.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserAccountsDto extends UserDto {
    private List<AccountDto> accounts = new ArrayList<>();

    public UserAccountsDto(User user) {
        super(user);
    }

    public List<AccountDto> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountDto> accounts) {
        this.accounts = accounts;
    }
}
