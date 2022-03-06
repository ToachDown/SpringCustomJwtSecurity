package com.example.jwtcleanspring.service;

import com.example.jwtcleanspring.dto.LoginDto;
import com.example.jwtcleanspring.model.JWT;
import com.example.jwtcleanspring.model.User;
import com.example.jwtcleanspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final JWTProvider jwtProvider;
    private final UserRepository userRepository;

    @Autowired
    public UserService(JWTProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserByUsername (String username) {
        return userRepository.findByUsername(username);
    }

    public ResponseEntity<String> verifyUser(LoginDto dto) {
        User user = userRepository.findByUsername(dto.getUsername());
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new IllegalArgumentException("invalid password");
        }
        JWT jwt = jwtProvider.extractData(user);
        String token = jwtProvider.generateToken(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        return ResponseEntity.ok()
                .headers(headers)
                .body("Successful login " + user.getUsername());
    }
}
