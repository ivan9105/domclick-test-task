package com.domclick.dto

import com.domclick.model.User
import lombok.Builder

import java.util.ArrayList

@Builder
class UserAccountsDto(user: User) : UserDto(user) {
    var accounts: ArrayList<AccountDto> = ArrayList()
}