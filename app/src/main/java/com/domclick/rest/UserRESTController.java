package com.domclick.rest;

import com.domclick.dto.UserAccountsDto;
import com.domclick.dto.response.UserResponse;
import com.domclick.exception.BadRequestException;
import com.domclick.model.User;
import com.domclick.repository.UserRepository;
import com.domclick.utils.DtoBuilder;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserRESTController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DtoBuilder dtoBuilder;

    @GetMapping("/list")
    public UserResponse userList() {
        return dtoBuilder.buildUserResponse(Lists.newArrayList(userRepository.findAll()));
    }

    @GetMapping("/get/{id}")
    public UserAccountsDto getUser(@PathVariable(name = "id") Long id) throws BadRequestException {
        User user = userRepository.findUserAccountsById(id);
        if (user == null) {
            throw new BadRequestException(String.format("User with id '%s' not found", id));
        }
        return dtoBuilder.buildUserAccountsDto(user);
    }
}
