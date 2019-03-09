package com.domclick.dto.response

import com.domclick.dto.UserDto
import java.io.Serializable

class UserResponse : Serializable {
    var users: MutableList<UserDto> = mutableListOf()
}
