package com.domclick.service

import com.domclick.entity.UserEntity

interface UserService : CrudService<UserEntity, Long> {
    fun findUserAccountsById(id: Long) : UserEntity?
}