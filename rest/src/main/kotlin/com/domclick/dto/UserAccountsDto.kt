package com.domclick.dto

import com.domclick.entity.User

class UserAccountsDto(user: User) : UserDto(user) {
    var accounts: MutableList<AccountDto> = mutableListOf()
}