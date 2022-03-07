package com.example.jwtcleanspring.controller;

import com.example.jwtcleanspring.annotation.Auth;
import com.example.jwtcleanspring.dto.LoginDto;
import com.example.jwtcleanspring.model.User;
import com.example.jwtcleanspring.model.enums.Role;
import com.example.jwtcleanspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody final LoginDto dto) {
        return userService.verifyUser(dto);
    }

    @Auth(roles = {Role.ADMIN})
    @GetMapping("/users")
    public List<User> getUsers () {
        return userService.getUsers();
    }
}
