package com.example.crud.comment.service;

import com.example.crud.board.domain.Board;
import com.example.crud.board.dto.BoardDto;
import com.example.crud.board.dto.BoardRequestDto;
import com.example.crud.board.repository.BoardRepository;
import com.example.crud.comment.domain.Comment;
import com.example.crud.comment.dto.CommentDto;
import com.example.crud.comment.dto.CommentRequestDto;
import com.example.crud.comment.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public List<CommentDto> findCommentsByBoardId(Long boardId) {
        return commentRepository.findCommentsByBoardId(boardId);
    }

    public CommentDto findByBoardIdAndId(Long boardId, Long id) {
        CommentDto comment = commentRepository.findByBoardIdAndId(boardId, id)
                .orElseThrow(() -> new IllegalArgumentException("Board NOT Found"));
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getWriter(),
                comment.getDate(),
                comment.getLikesCnt(),
                comment.getBoardId()
        );
    }

    public CommentDto saveComment(CommentRequestDto requestDto) {

        Board board = boardRepository.findById(requestDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Board Not Found"));


        Comment comment = new Comment();
        comment.setContent(requestDto.getContent());
        comment.setWriter(requestDto.getWriter());
        comment.setPassword(requestDto.getPassword());
        comment.setDate(LocalDateTime.now());
        comment.setBoard(board);

        Comment savedComment = commentRepository.save(comment);

        return new CommentDto(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getWriter(),
                savedComment.getDate(),
                savedComment.getLikesCnt(),
                savedComment.getBoard().getId()
        );
    }

    public CommentDto updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 댓글이 존재하지 않습니다."));

        comment.setContent(requestDto.getContent());
        comment.setDate(LocalDateTime.now());
        Comment updatedComment = commentRepository.save(comment);

        return new CommentDto(
                updatedComment.getId(),
                updatedComment.getContent(),
                updatedComment.getWriter(),
                updatedComment.getDate(),
                updatedComment.getLikesCnt(),
                updatedComment.getBoard().getId()
        );
    }

    public void deleteComment(Long boardId, Long commentId) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));

        // 댓글이 해당 게시글에 속하는지 확인
        if (!comment.getBoard().getId().equals(boardId)) {
            throw new IllegalArgumentException("The comment does not belong to the specified board.");
        }

        // 댓글 삭제
        commentRepository.delete(comment);
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
