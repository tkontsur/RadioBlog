package com.radioblog.service;

import com.radioblog.BaseSpringTest;
import com.radioblog.dto.UserDTO;
import com.radioblog.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest extends BaseSpringTest {
    @Autowired
    private UserService userService;

    @Test
    void isUsernameAvailable() {
        assertTrue(userService.isUsernameAvailable("test"));

        userService.createUser(new UserDTO(0, "test", User.Role.USER.toString()));

        assertFalse(userService.isUsernameAvailable("test"));
    }
}