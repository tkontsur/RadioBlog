package com.radioblog.service;

import com.radioblog.BaseSpringTest;
import com.radioblog.dto.LoginUserDTO;
import com.radioblog.entity.User;
import com.radioblog.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest extends BaseSpringTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private HttpServletRequest request;
    @MockBean
    private HttpServletResponse response;

    @Test
    void isUsernameAvailable() {
        assertTrue(userService.isUsernameAvailable("test"));

        userService.saveUser(new User().setUsername("test").setPassword("123").setRole(User.Role.USER));

        assertFalse(userService.isUsernameAvailable("test"));
    }

    @Test
    public void registerAndLogin() {
        User user = new User().setUsername("test1").setPassword("123").setRole(User.Role.USER);

        userService.saveUser(user);

        user = userRepository.findByUsername("test1").orElseThrow();
        assertNotEquals("123", user.getPassword());

        assertNotNull(userService.login(new LoginUserDTO("test1", "123"), request, response));
    }

    @Test
    public void wrongPassword() {
        User user = new User().setUsername("test2").setPassword("123").setRole(User.Role.USER);

        userService.saveUser(user);

        user = userRepository.findByUsername("test2").orElseThrow();
        assertNotEquals("123", user.getPassword());

        assertThrows(BadCredentialsException.class, () ->
                userService.login(new LoginUserDTO("test2", "wrong"), request, response));
    }

    @Test
    public void wrongUsername() {
        assertThrows(InternalAuthenticationServiceException.class, () ->
                userService.login(new LoginUserDTO("doesNotExist", "anypassword"), request, response));
    }
}