package com.domclick.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

public class AccountDepositRequest implements Serializable {
    @NotNull(message = "Field 'accountId' is required")
    @PositiveOrZero(message = "Field 'accountId' value must be positive")
    private Long accountId;
    @NotNull(message = "Field 'value' is required")
    @PositiveOrZero(message = "Field 'value' value must be positive")
    private Double value;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
