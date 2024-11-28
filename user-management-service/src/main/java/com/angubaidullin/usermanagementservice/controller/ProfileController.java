package com.angubaidullin.usermanagementservice.controller;

import com.angubaidullin.usermanagementservice.dto.AdminRequestUpdateUserDTO;
import com.angubaidullin.usermanagementservice.dto.UserRequestDTO;
import com.angubaidullin.usermanagementservice.dto.UserResponseDTO;
import com.angubaidullin.usermanagementservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;

    //Получение данных текущего пользователя
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUserProfile(Authentication authentication) {
        String email = authentication.getName();
        UserResponseDTO userResponseDTO = userService.findByEmail(email);
        return ResponseEntity.ok(userResponseDTO);
    }

    //Обновление данных текущего пользователя
    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUserProfile(
            Authentication authentication,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        String email = authentication.getName();
        userService.updateCurrentUser(email, userRequestDTO);
        return ResponseEntity.ok("Profile updated successfully");
    }

    //Получение списка пользователей (Админ)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //Получить данные пользователя по id (Админ)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    //Обновление профиля (Админ)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable Long id,
            @Valid @RequestBody AdminRequestUpdateUserDTO adminRequestUpdateUserDTO) {
        userService.updateUserByAdmin(id, adminRequestUpdateUserDTO);
        return ResponseEntity.ok("Users profile updated successfully");
    }

    //Удаление пользователя (Админ)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Users profile deleted successfully");
    }

}
