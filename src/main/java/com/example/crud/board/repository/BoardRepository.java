package com.example.crud.board.repository;

import com.example.crud.board.domain.Board;
import com.example.crud.board.dto.BoardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByUserIdAndId(Long userId, Long id);
    List<BoardDto> findBoardsByUserId(Long userId);
}