package com.radioblog.dto;

import com.radioblog.entity.User;

public record LoginUserDTO(String username, String password) {
    public User toUser() {
        return new User()
                .setUsername(username())
                .setPassword(password())
                .setRole(User.Role.USER);
    }
}
