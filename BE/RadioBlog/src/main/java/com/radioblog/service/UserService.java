package com.radioblog.service;

import com.radioblog.dto.LoginUserDTO;
import com.radioblog.dto.UserDTO;
import com.radioblog.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    boolean isUsernameAvailable(String username);

    void saveUser(User newUser);

    UserDTO login(LoginUserDTO userDTO, HttpServletRequest request, HttpServletResponse response);

    User getCurrentUser();

    UserDTO getCurrentUserDTO();
}
