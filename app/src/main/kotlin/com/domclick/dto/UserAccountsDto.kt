package com.domclick.dto

import com.domclick.model.User

import java.util.ArrayList

class UserAccountsDto(user: User) : UserDto(user) {
    var accounts: ArrayList<AccountDto> = ArrayList()
}