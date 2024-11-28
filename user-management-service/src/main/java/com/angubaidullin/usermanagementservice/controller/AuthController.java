package com.angubaidullin.usermanagementservice.controller;

import com.angubaidullin.usermanagementservice.dto.LoginRequestDTO;
import com.angubaidullin.usermanagementservice.dto.UserRequestDTO;
import com.angubaidullin.usermanagementservice.exception.AuthenticationFailedException;
import com.angubaidullin.usermanagementservice.security.jwt.JwtResponse;
import com.angubaidullin.usermanagementservice.service.UserService;
import com.angubaidullin.usermanagementservice.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        //Если пользователь с таким email уже зарегистрирован возвращаем ответ, что пользователь с таким email уже есть
        if (userService.existsByEmail(userRequestDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists: " + userRequestDTO.getEmail());
        }

        //Если пользователя с таким email нет, то сохраняем его в БД и отпрвляем ответ об успешно регистрации
        userService.save(userRequestDTO);
        return ResponseEntity.status(201).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),
                            loginRequestDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //Генерация JWT токена или другой логики аутентификации
            String jwt = jwtUtils.generateToken(authentication);

            return ResponseEntity.ok().body(new JwtResponse(jwt));
        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Ошибка: неверный логин или пароль");
        }
    }

}
