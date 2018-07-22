package com.domclick.dto.response

import com.domclick.dto.AccountDto
import lombok.Builder

import java.io.Serializable
import java.util.ArrayList

@Builder
class AccountResponse : Serializable {
    var accounts: ArrayList<AccountDto> = ArrayList()
}
