package com.domclick.service

import com.domclick.model.User

interface UserService : CrudService<User, Long> {
    fun findUserAccountsById(id: Long) : User
}