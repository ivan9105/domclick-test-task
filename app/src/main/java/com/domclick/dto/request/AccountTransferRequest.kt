package com.domclick.dto.request

import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero
import java.io.Serializable
import java.math.BigDecimal

class AccountTransferRequest : Serializable {
    @NotNull(message = "Field 'fromAccountId' is required")
    @PositiveOrZero(message = "Field 'fromAccountId' value must be positive")
    var fromAccountId: Long = 0
    @NotNull(message = "Field 'toAccountId' is required")
    @PositiveOrZero(message = "Field 'toAccountId' value must be positive")
    var toAccountId: Long = 0
    @NotNull(message = "Field 'value' is required")
    @PositiveOrZero(message = "Field 'value' value must be positive")
    lateinit var value: BigDecimal
}
