package com.angubaidullin.authservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Set<String> roles;
}
