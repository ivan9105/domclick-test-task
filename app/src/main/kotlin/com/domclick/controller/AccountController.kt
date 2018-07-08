package com.domclick.controller

import com.domclick.model.Account
import com.domclick.repository.AccountRepository
import com.domclick.repository.UserRepository
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
class AccountController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @GetMapping(value = ["/accounts"])
    fun accountsList(model: Model): String {
        model.addAttribute("accounts", accountRepository.findAll())
        return "account/list"
    }

    @GetMapping(value = arrayOf("/accounts/edit", "/accounts/edit/{id}"))
    fun accountEditForm(model: Model, @PathVariable(required = false, name = "id") id: Long?): String {
        val account = if (id != null) findAccountById(id) else Account()

        model.addAttribute("account", account)

        model.addAttribute("users", userRepository.findAll())

        account!!.updateUserId()
        return "account/edit"
    }

    @PostMapping(value = ["/accounts/edit"])
    fun doAccountEdit(model: Model, @Valid account: Account, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            return "account/edit"
        }

        val reload = findAccountById(account.id) ?: return "account/edit"

        reload.balance = account.balance
        reload.user = userRepository.findById(account.userId.toLong()).orElseThrow {
            RuntimeException(
                    format("Can not found account by id '%s'", account.user!!.id))
        }

        reload.updateUserId()

        accountRepository.save(reload)
        model.addAttribute("accounts", accountRepository.findAll())
        return "account/list"
    }

    @GetMapping(value = ["/accounts/delete/{id}"])
    fun accountDelete(model: Model, @PathVariable(name = "id") id: Long): String {
        val reload = findAccountById(id) ?: return "account/list"
        accountRepository.delete(reload)
        model.addAttribute("accounts", accountRepository.findAll())
        return "account/list"
    }

    private fun findAccountById(id: Long): Account? {
        return accountRepository.findById(id).orElseThrow {
            RuntimeException(
                    format("Can not found account by id '%s'", id))
        }
    }
}
