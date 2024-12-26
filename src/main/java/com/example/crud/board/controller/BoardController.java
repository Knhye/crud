package com.example.crud.board.controller;

import com.example.crud.board.dto.BoardDto;
import com.example.crud.board.dto.BoardRequestDto;
import com.example.crud.board.service.BoardService;
import com.example.crud.user.JwtUtil;
import com.example.crud.user.domain.UserEntity;
import com.example.crud.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 헤더에서 JWT 토큰 추출 메서드
    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    // 토큰 유효성 및 사용자 검증 메서드
    private void validateTokenAndGetUser(String token, Long userId) {
        if (token == null || !jwtUtil.isTokenValid(token)) {
            throw new RuntimeException("Invalid token");
        }

        String username = jwtUtil.extractUsername(token);
        if (username == null) {
            throw new RuntimeException("Invalid token");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to access this data");
        }

    }

    // 게시물 목록 조회
    @GetMapping
    public ResponseEntity<List<BoardDto>> getAllBoards(HttpServletRequest request) {
        // 서비스에 HttpServletRequest를 전달하여 게시글 조회
        List<BoardDto> boards = boardService.findBoardsByUserId(request);
        return ResponseEntity.ok(boards);
    }

    // 특정 게시물 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto> getUserBoard(@PathVariable Long boardId, HttpServletRequest request) {
        BoardDto board = boardService.findByUserIdAndBoardId(boardId, request);
        return ResponseEntity.ok(board);
    }

    // 게시물 저장
    @PostMapping
    public ResponseEntity<BoardDto> saveBoard(
            @RequestBody BoardRequestDto requestDto,
            HttpServletRequest request) {

        String token = extractTokenFromRequest(request);
        if (token == null || !jwtUtil.isTokenValid(token)) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        // JWT 토큰에서 사용자 이름 추출
        String username = jwtUtil.extractUsername(token);

        // 게시글 저장
        BoardDto savedBoard = boardService.saveBoard(requestDto, username);
        return ResponseEntity.ok(savedBoard);
    }


    // 게시물 수정
    @PutMapping("/{userId}/{boardId}")
    public ResponseEntity<BoardDto> updateBoard(
            @PathVariable Long userId,
            @PathVariable Long boardId,
            @RequestBody BoardRequestDto updatedRequestDto,
            HttpServletRequest request) {

        String token = extractTokenFromRequest(request);
        validateTokenAndGetUser(token, userId);

        BoardDto updatedBoard = boardService.updateBoard(boardId, updatedRequestDto, request);
        return ResponseEntity.ok(updatedBoard);
    }

    // 게시물 삭제
    @DeleteMapping("/{userId}/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable Long userId,
            @PathVariable Long boardId,
            HttpServletRequest request) {

        String token = extractTokenFromRequest(request);
        validateTokenAndGetUser(token, userId);

        boardService.deleteBoard(boardId, request);
        return ResponseEntity.noContent().build();
    }

    // 게시물 좋아요
    @PostMapping("/{boardId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long boardId) {
        boardService.likePost(boardId);
        return ResponseEntity.ok().build();
    }

    // 게시물 좋아요 취소
    @PostMapping("/{boardId}/unlike")
    public ResponseEntity<Void> unlikePost(@PathVariable Long boardId) {
        boardService.unlikePost(boardId);
        return ResponseEntity.ok().build();
    }
}
