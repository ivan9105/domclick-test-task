package com.domclick.controller;

import com.domclick.model.User;
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
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/users")
    public String usersList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }

    @GetMapping(value = {"/users/edit", "/users/edit/{id}"})
    public String userEditForm(Model model, @PathVariable(required = false, name = "id") Long id) {
        model.addAttribute("user", id != null ? findUserById(id) : new User());
        return "user/edit";
    }

    @PostMapping(value = "/users/edit")
    public String doUserEdit(Model model, @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        User reload = user.getId() != null ? findUserById(user.getId()) : null;
        if (reload != null) {
            reload.setMiddleName(user.getMiddleName());
            reload.setLastName(user.getLastName());
            reload.setFirstName(user.getFirstName());
            userRepository.save(reload);
        } else {
            userRepository.save(user);
        }
        return "redirect:/users";
    }

    @GetMapping(value = "/users/delete/{id}")
    public String userDelete(Model model, @PathVariable(name = "id") Long id) {
        userRepository.delete(findUserById(id));
        return "redirect:/users";
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException(
                format("Can not found user by id '%s'", id)));
    }
}
