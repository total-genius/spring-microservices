package com.angubaidullin.usermanagementservice.mapper;

import com.angubaidullin.usermanagementservice.dto.UserRequestDTO;
import com.angubaidullin.usermanagementservice.dto.UserResponseDTO;
import com.angubaidullin.usermanagementservice.entity.Role;
import com.angubaidullin.usermanagementservice.entity.User;
import com.angubaidullin.usermanagementservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserMapper {

    private final RoleRepository roleRepository;

    //Преобразуем User в UserResponseDTO
    public UserResponseDTO toUserResponseDTO(User user) {
        //Если пришло пустое значение
        if (user == null) {
            return null;
        }

        //Создаем UserResponseDTO и инициализируем его поля значениями полей user
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());

        //Из объекта типа Role вытаскиваем названия ролей
        userResponseDTO.setRoles(
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );

        return userResponseDTO;
    }

    //Преобразуем UserRequestDTO в User
    public User toUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return null;
        }

        //Создаем объект типа User и инициализируем его поля значениями полей userRequestDTO
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());

        //По названиям ролей получаем из БД роли
        if (userRequestDTO.getRoles() != null) {
            Set<Role> roles = userRequestDTO.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName).orElseThrow(() ->
                            new RuntimeException()))
                    .collect(Collectors.toSet());

            //Присваиваем полученные сущности Role объекту User
            user.setRoles(roles);

        } else {
            //Если в userRequestDTO роли пустые, присваиваем дефолтную роль "юзер"
            Role defaultRole = roleRepository.findByName("ROLE_USER").orElseThrow(() ->
                    new RuntimeException());
            user.setRoles(Set.of(defaultRole));
        }
        return user;
    }
}
