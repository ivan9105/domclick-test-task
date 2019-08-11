package com.domclick.dto

import com.domclick.entity.UserEntity
import java.io.Serializable

open class UserDto(user: UserEntity) : Serializable {
    var id: Long? = null
    var firstName: String? = null
    var lastName: String? = null
    var middleName: String? = null
    var links: MutableList<LinkDto> = mutableListOf()

    init {
        this.id = user.id
        this.firstName = user.firstName
        this.lastName = user.lastName
        this.middleName = user.middleName
    }
}
