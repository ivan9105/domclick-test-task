package com.domclick.rest

import com.domclick.dto.UserAccountsDto
import com.domclick.dto.response.UserResponse
import com.domclick.exception.BadRequestException
import com.domclick.repository.UserRepository
import com.domclick.utils.DtoBuilder
import com.google.common.collect.Lists
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserRESTController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var dtoBuilder: DtoBuilder

    @GetMapping("/list")
    fun userList(): UserResponse {
        return dtoBuilder.buildUserResponse(Lists.newArrayList(userRepository.findAll()))
    }

    @GetMapping("/get/{id}")
    @Throws(BadRequestException::class)
    fun getUser(@PathVariable(name = "id") id: Long): UserAccountsDto {
        val user = userRepository.findUserAccountsById(id)
        return dtoBuilder.buildUserAccountsDto(user)
    }
}
