package com.domclick.dto

import com.domclick.entity.User
import java.util.*

class UserAccountsDto(user: User) : UserDto(user) {
    var accounts: ArrayList<AccountDto> = ArrayList()
}