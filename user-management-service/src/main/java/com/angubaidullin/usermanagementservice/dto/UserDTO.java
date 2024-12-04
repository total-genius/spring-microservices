package com.angubaidullin.usermanagementservice.dto;

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
