package com.limon.library_app.controller;

import com.limon.library_app.payload.LoginRequest;
import com.limon.library_app.payload.LoginResponse;
import com.limon.library_app.payload.RegistrationRequest;
import com.limon.library_app.service.SessionService;
import com.limon.library_app.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationRequest request) {
        try {
            userService.registerUser(request);
            return new ResponseEntity<>("Registration was successful.", HttpStatus.CREATED);
        } catch (EntityExistsException ex) {
            return new ResponseEntity<>("The user is already registered with the provided email or username.", HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
            LoginResponse response = userService.login(request);

            String sessionToken = UUID.randomUUID().toString();
            sessionService.storeSession(sessionToken, response.getEmail());


            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, "session=" + sessionToken + "; HttpOnly; Path=/");

            return ResponseEntity.ok().headers(headers).body(response);
    }

}
