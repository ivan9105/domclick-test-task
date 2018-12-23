package com.domclick.rest

import com.domclick.dto.UserAccountsDto
import com.domclick.exception.BadRequestException
import com.domclick.service.UserService
import com.domclick.utils.DtoBuilder
import com.google.common.collect.Lists
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserRESTController(
        private val userService: UserService,
        private val dtoBuilder: DtoBuilder
) {

    @GetMapping("/list")
    fun userList() = dtoBuilder.buildUserResponse(Lists.newArrayList(userService.findAll()))

    @GetMapping("/get/{id}")
    @Throws(BadRequestException::class)
    fun getUser(@PathVariable(name = "id") id: Long): UserAccountsDto {
        val user = userService.findUserAccountsById(id)
        return dtoBuilder.buildUserAccountsDto(user)
    }
}
