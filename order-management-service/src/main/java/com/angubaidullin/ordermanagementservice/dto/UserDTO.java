package com.angubaidullin.ordermanagementservice.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String role;
}
