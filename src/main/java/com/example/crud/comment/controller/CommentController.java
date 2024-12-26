package com.example.crud.comment.controller;

import com.example.crud.board.dto.BoardDto;
import com.example.crud.comment.domain.Comment;
import com.example.crud.comment.dto.CommentDto;
import com.example.crud.comment.dto.CommentRequestDto;
import com.example.crud.comment.repository.CommentRepository;
import com.example.crud.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments/{boardId}")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getAllComments(@PathVariable Long boardId) {
        return commentService.findCommentsByBoardId(boardId);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable Long boardId, @PathVariable Long commentId) {

        CommentDto comment = commentService.findByBoardIdAndId(boardId, commentId);
        return ResponseEntity.ok(comment);
    }

    @PostMapping
    public ResponseEntity<CommentDto> saveComment(@PathVariable Long boardId, @RequestBody CommentRequestDto requestDto) {
        requestDto.setBoardId(boardId);
        CommentDto savedComment = commentService.saveComment(requestDto);
        return ResponseEntity.ok(savedComment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateBoard(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto updatedCommentDto) {

        CommentDto updatedComment = commentService.updateComment(commentId, updatedCommentDto);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long boardId, @PathVariable Long commentId) {
        commentService.deleteComment(boardId, commentId);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    @PostMapping("/{commentId}/like")
    public void likePost(@PathVariable Long commentId) {
        commentService.likePost(commentId);
    }

    @PostMapping("/{commentId}/unlike")
    public void unlikePost(@PathVariable Long commentId) {
        commentService.unlikePost(commentId);
    }
}
