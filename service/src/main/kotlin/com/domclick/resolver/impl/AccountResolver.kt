package com.domclick.resolver.impl

import com.domclick.entity.AccountEntity
import com.domclick.model.Account
import com.domclick.resolver.BaseResolver
import com.domclick.resolver.Context
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.MANDATORY
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(propagation = MANDATORY)
class AccountResolver: BaseResolver<AccountEntity, Account> {
    override fun toModel(entity: AccountEntity): Account {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toEntity(model: Account, context: Context): AccountEntity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}