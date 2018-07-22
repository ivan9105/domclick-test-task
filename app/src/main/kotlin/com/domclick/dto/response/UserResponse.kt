package com.domclick.dto.response

import com.domclick.dto.UserDto
import lombok.Builder

import java.io.Serializable
import java.util.ArrayList

@Builder
class UserResponse : Serializable {
    var users: ArrayList<UserDto> = ArrayList()
}
