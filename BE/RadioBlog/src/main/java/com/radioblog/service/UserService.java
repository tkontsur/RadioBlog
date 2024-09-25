package com.radioblog.service;

import com.radioblog.dto.UserDTO;

public interface UserService {
    boolean isUsernameAvailable(String username);

    void createUser(UserDTO userDTO);
}
