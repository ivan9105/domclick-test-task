package com.domclick.service

import com.domclick.entity.UserEntity
import com.domclick.exception.RollbackException
import com.domclick.repository.UserRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [(RollbackException::class)])
@Service
class UserServiceImpl(
        private val userRepository: UserRepository
) : CrudServiceImpl<UserEntity, Long>(), UserService {

    override fun upsert(entity: UserEntity) {
        val reload = if (entity.isNew()) UserEntity() else findUserAccountsById(entity.id!!)
        reload!!.middleName = entity.middleName
        reload.lastName = entity.lastName
        reload.firstName = entity.firstName
        save(reload)
    }

    override fun findUserAccountsById(id: Long) = userRepository.findUserAccountsById(id)

    override fun getRepository(): CrudRepository<UserEntity, Long> = userRepository
}