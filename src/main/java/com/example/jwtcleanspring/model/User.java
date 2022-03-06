package com.example.jwtcleanspring.model;

import com.example.jwtcleanspring.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private Role role;
    private String username;
    private String email;
    private String password;

}
