package com.domclick.resolver.impl

import com.domclick.entity.UserEntity
import com.domclick.model.User
import com.domclick.resolver.BaseResolver
import com.domclick.resolver.Context
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.MANDATORY
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(propagation = MANDATORY)
class UserResolver(
        private val accountResolver: AccountResolver
) : BaseResolver<UserEntity, User> {
    override fun toModel(entity: UserEntity): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toEntity(model: User, context: Context): UserEntity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //Todo fix service tests
}