package com.domclick.repository.utils

import com.domclick.entity.AccountEntity
import com.domclick.entity.UserEntity
import java.math.BigDecimal

fun getValidUser() = UserEntity("Иванов", "Иван", "Иванович")

fun getValidAccount(user: UserEntity) = AccountEntity(BigDecimal(10), user)