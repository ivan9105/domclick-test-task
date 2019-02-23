package com.domclick.repository.utils

import com.domclick.entity.Account
import com.domclick.entity.User
import java.math.BigDecimal

fun getValidUser() = User("Иванов", "Иван", "Иванович")

fun getValidAccount(user: User) = Account(BigDecimal(10), user)