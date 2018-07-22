package com.domclick.service

import com.domclick.exception.RollbackException
import com.domclick.model.User
import com.domclick.repository.UserRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [(RollbackException::class)])
@Service
class UserServiceImpl(private val userRepository: UserRepository) : CrudServiceImpl<User, Long>(), UserService {
    override fun findUserAccountsById(id: Long): User = userRepository.findUserAccountsById(id)

    override fun getRepository(): CrudRepository<User, Long> = userRepository
}