package com.domclick.dto

import com.domclick.entity.AccountEntity
import java.io.Serializable
import java.math.BigDecimal

class AccountDto(account: AccountEntity) : AbstractDto(), Serializable {
    var id: Long? = null
    var balance: BigDecimal? = null

    init {
        this.id = account.id
        this.balance = account.balance
    }
}
