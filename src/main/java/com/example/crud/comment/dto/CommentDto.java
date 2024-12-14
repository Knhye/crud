package com.example.crud.comment.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class CommentDto {
    private Long id;
    private String content;
    private String writer;
    private String password;
    private LocalDateTime date;
    private Long boardId;

    public CommentDto(Long id, LocalDateTime date, String content, String writer, String password) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.writer = writer;
        this.password = password;
    }

    public CommentDto(Long id, LocalDateTime date, String content) {
        this.id = id;
        this.date = date;
        this.content = content;
    }

    public CommentDto(String content) {
        this.content = content;
    }

    public CommentDto(Long id, LocalDateTime date, String writer, String password, String content, Long boardId) {
        this.id = id;
        this.date = date;
        this.writer = writer;
        this.password = password;
        this.content = content;
        this.boardId = boardId;
    }
}
