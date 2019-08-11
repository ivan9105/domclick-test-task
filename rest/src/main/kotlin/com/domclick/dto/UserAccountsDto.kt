package com.domclick.dto

import com.domclick.entity.UserEntity

class UserAccountsDto(user: UserEntity) : UserDto(user) {
    var accounts: MutableList<AccountDto> = mutableListOf()
}