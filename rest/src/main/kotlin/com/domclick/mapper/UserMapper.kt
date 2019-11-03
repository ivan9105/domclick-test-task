package com.domclick.mapper

import com.domclick.dto.UserDto
import com.domclick.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class UserMapper : AbstractMapper<UserEntity, UserDto>() {
    override fun getDtoClass() = UserDto::class.java

    override fun getEntityClass() = UserEntity::class.java

    override fun resourceName() = "user"
}