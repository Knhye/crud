package com.example.crud.comment.service;

import com.example.crud.board.dto.BoardRequestDto;
import com.example.crud.comment.domain.Comment;
import com.example.crud.comment.dto.CommentDto;
import com.example.crud.comment.dto.CommentRequestDto;
import com.example.crud.comment.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentDto> findCommentsByBoardId(Long boardId) {
        List<CommentDto> comments = commentRepository.findCommentsByBoardId(boardId);

        return comments;
    }

    public CommentDto findByBoardIdAndId(Long boardId, Long id) {
        Comment comment = commentRepository.findByBoardIdAndId(boardId, id)
                .orElseThrow(() -> new IllegalArgumentException("Board NOT Found"));
        return new CommentDto(comment.getId(),comment.getDate(), comment.getWriter(), comment.getPassword(), comment.getContent(),boardId);
    }

    public CommentDto saveComment(CommentRequestDto requestDto) {

        Comment comment = new Comment();
        comment.setContent(requestDto.getContent());
        comment.setDate(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return new CommentDto(
                savedComment.getId(),
                savedComment.getDate(),
                savedComment.getContent()
        );
    }

    public CommentDto updateComment(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 To-Do 항목이 존재하지 않습니다."));

        comment.setContent(comment.getContent());
        comment.setDate(LocalDateTime.now());
        Comment updatedComment = commentRepository.save(comment);

        return new CommentDto(updatedComment.getContent());
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public void likePost(Long boardId) {
        Comment comment = commentRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        comment.incrementLikes();
    }

    @Transactional
    public void unlikePost(Long boardId) {
        Comment comment = commentRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        comment.decrementLikes();
    }
}
