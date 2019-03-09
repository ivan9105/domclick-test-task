package com.domclick.dto.response

import com.domclick.dto.AccountDto
import java.io.Serializable

class AccountResponse : Serializable {
    var accounts: MutableList<AccountDto> = mutableListOf()
}
