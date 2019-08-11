package com.domclick.controller

import com.domclick.entity.UserEntity
import com.domclick.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import java.lang.String.format
import javax.validation.Valid

@Controller
class UserController(private val userService: UserService) {

    @GetMapping(value = ["/users"])
    fun usersList(model: Model): String {
        model.addAttribute("users", userService.findAll())
        return "user/list"
    }

    @GetMapping(value = ["/users/edit", "/users/edit/{id}"])
    fun userEditForm(model: Model, @PathVariable(required = false, name = "id") id: Long?): String {
        model.addAttribute("user", if (id != null) findUserById(id) else UserEntity())
        return "user/edit"
    }

    @PostMapping(value = ["/users/edit"])
    fun doUserEdit(model: Model, @Valid user: UserEntity, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            return "user/edit"
        }
        userService.upsert(user)
        return "redirect:/users"
    }

    @GetMapping(value = ["/users/delete/{id}"])
    fun userDelete(model: Model, @PathVariable(name = "id") id: Long?): String {
        userService.delete(findUserById(id))
        return "redirect:/users"
    }

    private fun findUserById(id: Long?) = userService.findById(id!!).orElseThrow {
            RuntimeException(
                    format("Can not found user by id '%s'", id))
        }

}
