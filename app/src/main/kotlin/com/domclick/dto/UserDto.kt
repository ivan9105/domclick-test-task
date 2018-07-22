package com.domclick.dto

import com.domclick.model.User
import lombok.Builder

import java.io.Serializable
import java.util.ArrayList

@Builder
open class UserDto(user: User) : Serializable {
    var id: Long? = null
    var firstName: String? = null
    var lastName: String? = null
    var middleName: String? = null
    var links: ArrayList<LinkDto> = ArrayList()

    init {
        this.id = user.id
        this.firstName = user.firstName
        this.lastName = user.lastName
        this.middleName = user.middleName
    }
}
