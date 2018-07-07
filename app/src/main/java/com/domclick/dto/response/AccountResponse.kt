package com.domclick.dto.response

import com.domclick.dto.AccountDto

import java.io.Serializable
import java.util.ArrayList

class AccountResponse : Serializable {
    var accounts: ArrayList<AccountDto> = ArrayList()
}
