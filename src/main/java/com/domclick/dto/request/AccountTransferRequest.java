package com.domclick.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;

public class AccountTransferRequest implements Serializable {
    @NotNull(message = "Field 'fromAccountId' is required")
    @PositiveOrZero(message = "Field 'fromAccountId' value must be positive")
    private Long fromAccountId;
    @NotNull(message = "Field 'toAccountId' is required")
    @PositiveOrZero(message = "Field 'toAccountId' value must be positive")
    private Long toAccountId;
    @NotNull(message = "Field 'value' is required")
    @PositiveOrZero(message = "Field 'value' value must be positive")
    private BigDecimal value;

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
