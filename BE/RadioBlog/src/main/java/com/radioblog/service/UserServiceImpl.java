package com.radioblog.service;

import com.radioblog.controller.ResponseException;
import com.radioblog.dto.LoginUserDTO;
import com.radioblog.dto.UserDTO;
import com.radioblog.entity.User;
import com.radioblog.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    @Override
    @Transactional
    public void saveUser(User newUser) {
        if (!isUsernameAvailable(newUser.getUsername())) {
            throw new ResponseException(409, "Username is already taken");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        userRepository.save(newUser);
    }

    @Override
    public UserDTO login(LoginUserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(userDTO.username(), userDTO.password());
        Authentication authentication =
                this.authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return new UserDTO(user.getId(), user.getUsername(), user.getRole().toString());
        }

        return null;
    }
}
