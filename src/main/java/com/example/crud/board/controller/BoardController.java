package com.example.crud.board.controller;

import com.example.crud.board.dto.BoardDto;
import com.example.crud.board.dto.BoardRequestDto;
import com.example.crud.board.service.BoardService;
import com.example.crud.user.domain.UserEntity;
import com.example.crud.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserRepository userRepository;

    @GetMapping
    public List<BoardDto> getAllBoards() {
        return boardService.findBoardsByUserId();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto> getUserBoard(@PathVariable Long boardId) {
        BoardDto board = boardService.findByUserIdAndBoardId(boardId);
        return ResponseEntity.ok(board);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<BoardDto> saveBoard(@PathVariable Long userId, @RequestBody BoardRequestDto requestDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 인증되지 않으면 "guest"로 처리
        String username = (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : "guest";

        System.out.println(username);
        // 요청 받은 userId로 유저 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(user);
        // userId와 username이 일치하는지 확인
        if (user.getUsername().equals(username)) {
            BoardDto savedBoard = boardService.saveBoard(requestDto, userId, username);
            System.out.println(savedBoard);
            return ResponseEntity.ok(savedBoard);
        }
        throw new RuntimeException("You are not authorized to save data for this user");
    }

    @PutMapping("/{userId}/{boardId}")
    public ResponseEntity<BoardDto> updateBoard(
            @RequestBody BoardDto updatedBoardDto) {
        BoardDto updatedBoard = boardService.updateBoard(updatedBoardDto);
        return ResponseEntity.ok(updatedBoard);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{boardId}/like")
    public void likePost(@PathVariable Long boardId) {
        boardService.likePost(boardId);
    }

    @PostMapping("/{boardId}/unlike")
    public void unlikePost(@PathVariable Long boardId) {
        boardService.unlikePost(boardId);
    }
}
