package com.example.crud.comment.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class CommentRequestDto {
    private String content;
    private String writer;
    private String password;
    private Long boardId;
}
