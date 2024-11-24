package com.limon.library_app.service;

import com.limon.library_app.exception.UnauthorizedException;
import com.limon.library_app.payload.LoginRequest;
import com.limon.library_app.payload.LoginResponse;
import com.limon.library_app.payload.RegistrationRequest;
import com.limon.library_app.entity.User;
import com.limon.library_app.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void registerUser(RegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent() ||
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EntityExistsException("Username or email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> dbUser = userRepository.findByUsername(request.getUsername());

        if (dbUser.isPresent()) {
            User user = dbUser.get();

            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return new LoginResponse(user.getUsername(), user.getEmail());
            }
        }

        throw new UnauthorizedException("Authentication Failed");
    }



}
