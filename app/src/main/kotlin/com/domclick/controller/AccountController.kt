package com.domclick.controller

import com.domclick.entity.AccountEntity
import com.domclick.exception.BadRequestException
import com.domclick.service.AccountService
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
class AccountController(
        private val userService: UserService,
        private val accountService: AccountService
) {

    @GetMapping(value = ["/accounts"])
    fun accountsList(model: Model): String {
        model.addAttribute("accounts", accountService.findAll())
        return "account/list"
    }

    @GetMapping(value = ["/accounts/edit", "/accounts/edit/{id}"])
    fun accountEditForm(model: Model, @PathVariable(required = false, name = "id") id: Long?): String {
        val account = if (id != null) findAccountById(id) else AccountEntity()

        model.addAttribute("account", account)
        model.addAttribute("users", userService.findAll())
        if (account.user != null) account.updateUserId()
        return "account/edit"
    }

    @PostMapping(value = ["/accounts/edit"])
    fun doAccountEdit(model: Model, @Valid account: AccountEntity, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            return "account/edit"
        }

        accountService.upsert(account)

        model.addAttribute("accounts", accountService.findAll())
        return "account/list"
    }

    @GetMapping(value = ["/accounts/delete/{id}"])
    fun accountDelete(model: Model, @PathVariable(name = "id") id: Long): String {
        val reload = findAccountById(id) ?: return "account/list"
        accountService.delete(reload)
        model.addAttribute("accounts", accountService.findAll())
        return "account/list"
    }

    private fun findAccountById(id: Long) =
            accountService.findById(id).orElseThrow {
                throw BadRequestException(format("AccountEntity with id '%s' not found", id))
            }
}
