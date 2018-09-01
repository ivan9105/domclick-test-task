package com.domclick.dto.response

import com.domclick.dto.UserDto
import java.io.Serializable
import java.util.*

class UserResponse : Serializable {
    var users: ArrayList<UserDto> = ArrayList()
}
