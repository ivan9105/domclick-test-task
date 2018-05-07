package com.domclick.controller;

import com.domclick.model.Account;
import com.domclick.repository.AccountRepository;
import com.domclick.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

import static java.lang.String.format;

@Controller
public class AccountController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping(value = "/accounts")
    public String accountsList(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "account/list";
    }

    @GetMapping(value = {"/accounts/edit", "/accounts/edit/{id}"})
    public String accountEditForm(Model model, @PathVariable(required = false, name = "id") Long id) {
        Account account = id != null ? findAccountById(id) : new Account();
        account.updateUserId();
        model.addAttribute("account", account);

        model.addAttribute("users", userRepository.findAll());
        return "account/edit";
    }

    @PostMapping(value = "/accounts/edit")
    public String doAccountEdit(Model model, @Valid Account account, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "account/edit";
        }

        Account reload = account.getId() != null ? findAccountById(account.getId()) : null;
        if (reload != null) {
            reload.setBalance(account.getBalance());
            reload.setUser(userRepository.findById(account.getUserId()).orElseThrow(() -> new RuntimeException(
                    format("Can not found account by id '%s'", account.getUserId()))));
            accountRepository.save(reload);
        } else {
            account.setUser(userRepository.findById(account.getUserId()).orElseThrow(() -> new RuntimeException(
                    format("Can not found account by id '%s'", account.getUserId()))));
            accountRepository.save(account);
        }
        model.addAttribute("accounts", accountRepository.findAll());
        return "account/list";
    }

    @GetMapping(value = "/accounts/delete/{id}")
    public String accountDelete(Model model, @PathVariable(name = "id") Long id) {
        accountRepository.delete(findAccountById(id));
        model.addAttribute("accounts", accountRepository.findAll());
        return "account/list";
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException(
                format("Can not found account by id '%s'", id)));
    }
}
