package com.example.crud.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
    private static final int SECRET_KEY_LENGTH = 32; // 256비트 (32바이트)

    private SecretKey signingKey;

    @Value("${jwt.secret-key:}") // 프로퍼티에서 비밀 키를 읽어옴
    private String secretKeyBase64;

    @PostConstruct
    public void init() {
        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            byte[] keyBytes = generateSecretKeyBytes();
            secretKeyBase64 = Base64.getEncoder().encodeToString(keyBytes);
        }
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyBase64);
        signingKey = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.getJcaName());
    }

    // 256비트 강력한 비밀 키 생성 (바이트 배열 반환)
    private byte[] generateSecretKeyBytes() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[SECRET_KEY_LENGTH];
        random.nextBytes(key);
        return key;
    }

    // JWT 토큰 생성
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(signingKey)
                .compact();
    }

    // JWT 토큰에서 사용자 이름 추출
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // JWT 토큰 유효성 검사
    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false; // 파싱 오류 또는 유효하지 않은 토큰
        }
    }
}
