package com.angubaidullin.usermanagementservice.service;

import com.angubaidullin.usermanagementservice.dto.UserDTO;

public interface UserService {

    void createUser(UserDTO userDTO);
    UserDTO getUserByEmail(String email);
    boolean existsByEmail(String email);

}
