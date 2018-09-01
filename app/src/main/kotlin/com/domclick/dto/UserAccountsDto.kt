package com.domclick.dto

import com.domclick.model.User
import java.util.*

class UserAccountsDto(user: User) : UserDto(user) {
    var accounts: ArrayList<AccountDto> = ArrayList()
}