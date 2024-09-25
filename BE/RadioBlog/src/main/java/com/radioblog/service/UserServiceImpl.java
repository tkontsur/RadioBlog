package com.radioblog.service;

import com.radioblog.dto.UserDTO;
import com.radioblog.entity.User;
import com.radioblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    @Override
    public void createUser(UserDTO userDTO) {
        userRepository.save(new User(userDTO));
    }
}
