package com.domclick.dto.request

import lombok.Builder
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero
import java.io.Serializable
import java.math.BigDecimal

@Builder
class AccountWithdrawRequest : Serializable {
    @NotNull(message = "Field 'accountId' is required")
    @PositiveOrZero(message = "Field 'accountId' value must be positive")
    var accountId: Long = 0
    @NotNull(message = "Field 'value' is required")
    @PositiveOrZero(message = "Field 'value' value must be positive")
    lateinit var value: BigDecimal
}
