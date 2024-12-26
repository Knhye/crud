package com.example.crud.comment.repository;

import com.example.crud.comment.domain.Comment;
import com.example.crud.comment.dto.CommentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT new com.example.crud.comment.dto.CommentDto(c.id, c.content, c.writer, c.date, c.likesCnt, c.board.id) "
            + "FROM Comment c WHERE c.board.id = :boardId AND c.id = :commentId")
    Optional<CommentDto> findByBoardIdAndId(@Param("boardId") Long boardId, @Param("commentId") Long commentId);

    @Query("SELECT new com.example.crud.comment.dto.CommentDto(c.id, c.content, c.writer, c.date, c.likesCnt, c.board.id) "
            + "FROM Comment c WHERE c.board.id = :boardId")
    List<CommentDto> findCommentsByBoardId(@Param("boardId") Long boardId);
}
