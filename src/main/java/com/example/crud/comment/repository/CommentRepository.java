package com.example.crud.comment.repository;

import com.example.crud.comment.domain.Comment;
import com.example.crud.comment.dto.CommentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByBoardIdAndId(Long boardId, Long id);
    List<CommentDto> findCommentsByBoardId(Long boardId);
}
