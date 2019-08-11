package com.domclick.dto

import com.domclick.entity.AccountEntity
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

class AccountDto(account: AccountEntity) : Serializable {
    var id: Long? = null
    var balance: BigDecimal? = null
    var links: ArrayList<LinkDto> = ArrayList()

    init {
        this.id = account.id
        this.balance = account.balance
    }
}
