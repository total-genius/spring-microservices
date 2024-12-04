package com.angubaidullin.authservice.controller;

import com.angubaidullin.authservice.client.UserClient;
import com.angubaidullin.authservice.dto.JwtResponse;
import com.angubaidullin.authservice.dto.LoginRequest;
import com.angubaidullin.authservice.dto.SignupRequest;
import com.angubaidullin.authservice.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserClient userClient;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        // Проверяем, существует ли пользователь
        if (userClient.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }
        // Создаем пользователя в User Management Service
        userClient.createUser(signupRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(
                    userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map(grantedAuthority -> grantedAuthority.getAuthority())
                            .collect(Collectors.toList())
            );
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception e) {
            throw new AuthenticationServiceException("Invalid email or password");
        }
    }
}
