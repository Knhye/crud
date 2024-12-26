package com.example.crud.board.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class BoardDto {
    private Long id;
    private LocalDateTime date;
    private String title;
    private String content;
    private String tag;
    private int likesCnt;
    private Long userId;
//
//    public BoardDto(Long id, LocalDateTime date, String title, String content, Long userId) {
//        this.id = id;
//        this.date = date;
//        this.title = title;
//        this.content = content;
//        this.userId = userId;
//    }
//
//    public BoardDto(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
//
//    public BoardDto(Long id, String title, String content, Long userId) {
//        this.id = id;
//        this.title = title;
//        this.content = content;
//        this.userId = userId;
//    }
//
//    public BoardDto(Long id, LocalDateTime date, String title, String content, String tag, Long userId) {
//        this.id = id;
//        this.date = date;
//        this.title = title;
//        this.content = content;
//        this.tag = tag;
//        this.userId = userId;
//    }
}

