package com.radioblog.controller;

import com.radioblog.dto.LoginUserDTO;
import com.radioblog.dto.UserDTO;
import com.radioblog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody LoginUserDTO registerUserDTO, HttpServletRequest request, HttpServletResponse response) {
        userService.saveUser(registerUserDTO.toUser());
        return userService.login(registerUserDTO, request, response);
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginUserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
        return userService.login(userDTO, request, response);
    }

    @GetMapping("/me")
    public UserDTO me() {
        return userService.getCurrentUserDTO();
    }
}
