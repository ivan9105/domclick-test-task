package com.domclick.controller

import com.domclick.model.User
import com.domclick.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

import javax.validation.Valid

import java.lang.String.format

@Controller
class UserController(private var userRepository: UserRepository) {

    @GetMapping(value = ["/users"])
    fun usersList(model: Model): String {
        model.addAttribute("users", userRepository.findAll())
        return "user/list"
    }

    @GetMapping(value = ["/users/edit", "/users/edit/{id}"])
    fun userEditForm(model: Model, @PathVariable(required = false, name = "id") id: Long?): String {
        model.addAttribute("user", if (id != null) findUserById(id) else User())
        return "user/edit"
    }

    @PostMapping(value = ["/users/edit"])
    fun doUserEdit(model: Model, @Valid user: User, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            return "user/edit"
        }

        val reload = if (user.isNew()) User() else findUserById(user.id)
        reload.middleName = user.middleName
        reload.lastName = user.lastName
        reload.firstName = user.firstName
        userRepository.save(reload)
        return "redirect:/users"
    }

    @GetMapping(value = ["/users/delete/{id}"])
    fun userDelete(model: Model, @PathVariable(name = "id") id: Long?): String {
        userRepository.delete(findUserById(id))
        return "redirect:/users"
    }

    private fun findUserById(id: Long?) = userRepository.findById(id!!).orElseThrow {
            RuntimeException(
                    format("Can not found user by id '%s'", id))
        }

}
