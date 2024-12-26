package com.example.crud.board.service;

import com.example.crud.board.domain.Board;
import com.example.crud.board.dto.BoardDto;
import com.example.crud.board.dto.BoardRequestDto;
import com.example.crud.board.repository.BoardRepository;
import com.example.crud.user.JwtUtil;
import com.example.crud.user.domain.UserEntity;
import com.example.crud.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null || !jwtUtil.isTokenValid(token)) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
        String username = jwtUtil.extractUsername(token);

        return userRepository.findByUsername(username)
                .map(UserEntity::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<BoardDto> findBoardsByUserId(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);

        return boardRepository.findBoardsByUserId(userId).stream()
                .map(board -> new BoardDto(
                        board.getId(),
                        board.getDate(),
                        board.getTitle(),
                        board.getContent(),
                        board.getTag(),
                        board.getLikesCnt(),
                        board.getUser().getId()
                ))
                .collect(Collectors.toList());
    }

    public BoardDto findByUserIdAndBoardId(Long boardId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);

        Board board = boardRepository.findByIdAndUserId(boardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        return new BoardDto(
                board.getId(),
                board.getDate(),
                board.getTitle(),
                board.getContent(),
                board.getTag(),
                board.getLikesCnt(),
                board.getUser().getId()
        );
    }

    public BoardDto saveBoard(BoardRequestDto requestDto, String username) {
        // username으로 사용자 조회
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 게시글 생성 및 저장
        Board board = new Board();
        board.setTitle(requestDto.getTitle());
        board.setContent(requestDto.getContent());
        board.setTag(requestDto.getTag());
        board.setDate(LocalDateTime.now());
        board.setUser(user);

        Board savedBoard = boardRepository.save(board);

        // 저장된 게시글 DTO로 반환
        return new BoardDto(
                savedBoard.getId(),
                savedBoard.getDate(),
                savedBoard.getTitle(),
                savedBoard.getContent(),
                savedBoard.getTag(),
                savedBoard.getLikesCnt(),
                savedBoard.getUser().getId()
        );
    }


    public BoardDto updateBoard(Long boardId, BoardRequestDto requestDto, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);

        Board board = boardRepository.findByIdAndUserId(boardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        board.setTitle(requestDto.getTitle());
        board.setContent(requestDto.getContent());
        board.setTag(requestDto.getTag());
        board.setDate(LocalDateTime.now());

        Board updatedBoard = boardRepository.save(board);

        return new BoardDto(
                updatedBoard.getId(),
                updatedBoard.getDate(),
                updatedBoard.getTitle(),
                updatedBoard.getContent(),
                updatedBoard.getTag(),
                updatedBoard.getLikesCnt(),
                updatedBoard.getUser().getId()
        );
    }

    public void deleteBoard(Long boardId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);

        Board board = boardRepository.findByIdAndUserId(boardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        boardRepository.delete(board);
    }

    @Transactional
    public void likePost(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        board.incrementLikes();
    }

    @Transactional
    public void unlikePost(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        board.decrementLikes();
    }
}
