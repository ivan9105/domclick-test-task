package com.domclick.service

import com.domclick.entity.User

interface UserService : CrudService<User, Long> {
    fun findUserAccountsById(id: Long) : User
}