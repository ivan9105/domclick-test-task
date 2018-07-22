package com.domclick.dto

import com.domclick.model.Account
import lombok.Builder

import java.io.Serializable
import java.math.BigDecimal
import java.util.ArrayList

@Builder
class AccountDto(account: Account) : Serializable {
    var id: Long? = null
    var balance: BigDecimal? = null
    var links: ArrayList<LinkDto> = ArrayList()

    init {
        this.id = account.id
        this.balance = account.balance
    }
}
