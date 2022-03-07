package com.example.jwtcleanspring.repository;

import com.example.jwtcleanspring.model.User;
import com.example.jwtcleanspring.model.enums.Role;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {

    private final List<User> users;

    public UserRepository() {
        this.users = List.of(
                new User(1L, Role.ADMIN, "Sergey", "serg@email.com", "12345"),
                new User(2L, Role.USER, "Timmy", "tim@email.com", "1234512345"),
                new User(3L, Role.OPERATOR, "Pavel", "pavel@email.com", "11111")
        );
    }

    public List<User> getAllUsers() {
        return users;
    }

    public Optional<User> findByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}
