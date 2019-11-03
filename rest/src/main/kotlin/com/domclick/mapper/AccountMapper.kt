package com.domclick.mapper

import com.domclick.dto.AccountDto
import com.domclick.entity.AccountEntity
import org.springframework.stereotype.Component

@Component
class AccountMapper : AbstractMapper<AccountEntity, AccountDto>() {
    override fun getDtoClass() = AccountDto::class.java

    override fun getEntityClass() = AccountEntity::class.java

    override fun resourceName() = "account"
}