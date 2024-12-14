package com.example.crud.user.service;

import com.example.crud.user.domain.UserEntity;
import com.example.crud.user.dto.UserRequestDto;
import com.example.crud.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntity save(UserRequestDto dto) {
        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .username(dto.getUsername())
                // 패스워드 암호화
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .auth("ROLE_USER")  // 기본 권한 설정
                .build());
        return userEntity;
    }


    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
