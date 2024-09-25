package com.radioblog.service;

import com.radioblog.dto.UserDTO;
import com.radioblog.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void isUsernameAvailable() {
        assertTrue(userService.isUsernameAvailable("test"));

        userService.createUser(new UserDTO(0, "test", User.Role.USER.toString()));

        assertFalse(userService.isUsernameAvailable("test"));
    }
}