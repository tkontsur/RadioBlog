package com.radioblog.controller;

import com.radioblog.dto.LoginUserDTO;
import com.radioblog.dto.UserDTO;
import com.radioblog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> register(@RequestBody LoginUserDTO registerUserDTO) {
        userService.saveUser(registerUserDTO.toUser());

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginUserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
        return userService.login(userDTO, request, response);
    }
}
