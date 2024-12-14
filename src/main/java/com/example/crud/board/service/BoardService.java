package com.example.crud.board.service;

import com.example.crud.board.domain.Board;
import com.example.crud.board.dto.BoardDto;
import com.example.crud.board.dto.BoardRequestDto;
import com.example.crud.board.repository.BoardRepository;
import com.example.crud.user.domain.UserEntity;
import com.example.crud.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    // 현재 로그인한 사용자의 ID 가져오기
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();  // 인증된 사용자 이름 반환
        }
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getId();
    }

    public List<BoardDto> findBoardsByUserId() {
        Long userId = getCurrentUserId();
        List<BoardDto> boards = boardRepository.findBoardsByUserId(userId);

        // 엔티티를 DTO로 변환하여 반환
        return boards;
    }

    public BoardDto findByUserIdAndBoardId(Long id) {
        Long userId = getCurrentUserId();
        Board board = boardRepository.findByUserIdAndId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Board NOT Found"));
        return new BoardDto(board.getId(), board.getDate(), board.getTitle(), board.getContent(), board.getTag(), userId);
    }

    public BoardDto saveBoard(BoardRequestDto requestDto, Long userId, String username) {

//        UserEntity user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!user.getUsername().equals(username)) {
//            throw new RuntimeException("You are not authorized to save data for this user");
//        }

        // userId로 유저 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 인증되지 않은 사용자는 "guest"로 처리
        if (username == null || username.equals("anonymousUser")) {
            username = "guest"; // 기본 사용자 이름을 "guest"로 설정
        }

        // 요청받은 userId의 username과 인증된 username이 일치하는지 확인
        if (!user.getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to save data for this user");
        }


        Board board = new Board();
        board.setTitle(requestDto.getTitle());
        board.setContent(requestDto.getContent());
        board.setDate(LocalDateTime.now());
        board.setUser(user);

        Board savedBoard = boardRepository.save(board);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return new BoardDto(
                savedBoard.getId(),
                savedBoard.getDate(),
                savedBoard.getTitle(),
                savedBoard.getContent(),
                savedBoard.getUser().getId()
        );
    }

    public BoardDto updateBoard(BoardDto boardDto) {
        Board board = boardRepository.findById(boardDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 To-Do 항목이 존재하지 않습니다."));
        board.setTitle(boardDto.getTitle());
        board.setContent(board.getContent());
        board.setDate(LocalDateTime.now());
        Board updatedBoard = boardRepository.save(board);

        return new BoardDto(updatedBoard.getTitle(), updatedBoard.getContent());
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public void likePost(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        board.incrementLikes();
    }

    @Transactional
    public void unlikePost(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        board.decrementLikes();
    }
}
