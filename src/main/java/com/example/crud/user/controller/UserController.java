package com.example.crud.user.controller;

import com.example.crud.user.domain.UserEntity;
import com.example.crud.user.dto.UserRequestDto;
import com.example.crud.user.service.UserDetailService;
import com.example.crud.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDetailService userDetailService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserRequestDto dto, HttpSession session) {
        UserEntity user = userService.save(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Signup successful");
        response.put("username", dto.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid  @RequestBody UserRequestDto dto, HttpSession session) {
        UserDetails user = userDetailService.loadUserByUsername(dto.getUsername());
        if (user != null && userService.checkPassword(dto.getPassword(), user.getPassword())) {
            session.setAttribute("username", dto.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("status", 200);
            response.put("message", "Signup successful");
            response.put("username", dto.getUsername());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).build(); // 인증 실패
    }


}
