package com.example.crud.user.controller;

import com.example.crud.user.JwtUtil;
import com.example.crud.user.domain.UserEntity;
import com.example.crud.user.dto.UserDto;
import com.example.crud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto) {
        UserEntity savedUser = userService.registerUser(userDto);
        Map<String, String> response = new HashMap<>();
        response.put("username", savedUser.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        UserEntity foundUser = userService.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!userService.validatePassword(userDto.getPassword(), foundUser.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(foundUser.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", foundUser.getUsername());
        return ResponseEntity.ok(response);
    }
}
