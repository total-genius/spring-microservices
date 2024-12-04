package com.angubaidullin.authservice.client;

import com.angubaidullin.authservice.dto.SignupRequest;
import com.angubaidullin.authservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-management-service")
public interface UserClient {
    @PostMapping("/users")
    void createUser(@RequestBody SignupRequest signupRequest);

    @GetMapping("/users/email/{email}")
    UserDTO getUserByEmail(@PathVariable String email);

    @GetMapping("/users/exists/{email}")
    boolean existsByEmail(@PathVariable String email);
}
