package com.example.crud.comment.controller;

import com.example.crud.comment.dto.CommentDto;
import com.example.crud.comment.dto.CommentRequestDto;
import com.example.crud.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getAllComments(Long boardId) {
        return commentService.findCommentsByBoardId(boardId);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable Long boardId, @PathVariable Long id) {
        CommentDto comment = commentService.findByBoardIdAndId(boardId, id);
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/{boardId}")
    public ResponseEntity<CommentDto> saveComment(@RequestBody CommentRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CommentDto savedComment = commentService.saveComment(requestDto);
        return ResponseEntity.ok(savedComment);
    }

    @PutMapping("/{boardId}/{commentId}")
    public ResponseEntity<CommentDto> updateBoard(
            @RequestBody CommentDto updatedCommentDto) {
        CommentDto updatedComment = commentService.updateComment(updatedCommentDto);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{boardId}/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{boardId}/like")
    public void likePost(@PathVariable Long commentId) {
        commentService.likePost(commentId);
    }

    @PostMapping("/{boardId}/unlike")
    public void unlikePost(@PathVariable Long commentId) {
        commentService.unlikePost(commentId);
    }
}
