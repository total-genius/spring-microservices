package com.angubaidullin.usermanagementservice.service.impl;

import com.angubaidullin.usermanagementservice.dto.AdminRequestUpdateUserDTO;
import com.angubaidullin.usermanagementservice.dto.UserRequestDTO;
import com.angubaidullin.usermanagementservice.dto.UserResponseDTO;
import com.angubaidullin.usermanagementservice.entity.User;
import com.angubaidullin.usermanagementservice.exception.NotFoundWithSuchIdException;
import com.angubaidullin.usermanagementservice.exception.UserAlreadyExistsException;
import com.angubaidullin.usermanagementservice.mapper.UserMapper;
import com.angubaidullin.usermanagementservice.repository.RoleRepository;
import com.angubaidullin.usermanagementservice.repository.UserRepository;
import com.angubaidullin.usermanagementservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;


    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponseDTO)
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        return userMapper.toUserResponseDTO(userRepository.findById(id).orElseThrow(() -> new NotFoundWithSuchIdException("User with id " + id + " not found")));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void save(UserRequestDTO userRequestDTO) {
        User user = userMapper.toUser(userRequestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public UserResponseDTO findByEmail(String email) {
        return userMapper.toUserResponseDTO(userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException()));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void updateUserByAdmin(Long id, AdminRequestUpdateUserDTO adminRequestUpdateUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundWithSuchIdException("User with id " + id + " not found"));

        if (adminRequestUpdateUserDTO.getEnabled() != null) {
            user.setEnabled(adminRequestUpdateUserDTO.getEnabled());
        }

        if (adminRequestUpdateUserDTO.getRolesToAdd() != null && !adminRequestUpdateUserDTO.getRolesToAdd().isEmpty()) {
            adminRequestUpdateUserDTO.getRolesToAdd().stream()
                    .map(role -> roleRepository.findByName(role).get())
                    .forEach(role -> user.getRoles().add(role));
        }

        if (adminRequestUpdateUserDTO.getRolesToRemove() != null && !adminRequestUpdateUserDTO.getRolesToRemove().isEmpty()) {
            adminRequestUpdateUserDTO.getRolesToRemove().stream()
                    .map(role -> roleRepository.findByName(role).get())
                    .forEach(role -> user.getRoles().remove(role));
        }
        userRepository.save(user);
    }

    @Override
    public void updateCurrentUser(String email, UserRequestDTO userRequestDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundWithSuchIdException("User with email " + email + " not found"));

        //Если меняем email, то проверяем его уникальность
        if (!user.getEmail().equals(userRequestDTO.getEmail())) {
            if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
                throw new UserAlreadyExistsException("Email " + userRequestDTO.getEmail() + " already in use");
            }
            user.setEmail(userRequestDTO.getEmail());
        }

        user.setName(userRequestDTO.getName());

        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }
        userRepository.save(user);
    }


}
