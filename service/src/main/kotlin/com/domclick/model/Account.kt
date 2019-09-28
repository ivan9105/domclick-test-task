package com.domclick.model

import java.math.BigDecimal

data class Account(
        val id: Long,
        val balance: BigDecimal = BigDecimal(0),
        val userId: Long? = null,
        val userStr: String? = null
)