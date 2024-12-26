package com.example.crud.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private String writer;
    private LocalDateTime date;
    private int likesCnt;
    private Long boardId;
}
