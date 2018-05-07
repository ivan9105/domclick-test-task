package com.domclick.utils;

import com.domclick.dto.*;
import com.domclick.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@Scope("singleton")
public class DtoBuilder {
    @Value("${server.url}")
    private String serverUrl;

    public UserResponse buildUserResponse(List<User> users) {
        UserResponse response = new UserResponse();
        users.forEach(user -> {
            UserDto userDto = new UserDto(user);
            userDto.getLinks().add(new LinkDto("self", serverUrl + "user/get/" + user.getId()));
            response.getUsers().add(userDto);
        });

        return response;
    }

    //Todo add account dto build and operation lists
    public UserAccountsDto buildUserAccounts(User user) {
        UserAccountsDto res = new UserAccountsDto(user);
        res.getLinks().add(new LinkDto("self", serverUrl + "user/get/" + user.getId()));
        if (!CollectionUtils.isEmpty(user.getAccounts())) {
            user.getAccounts().forEach(account -> res.getAccounts().add(new AccountDto(account)));
        }
        return res;
    }
}
