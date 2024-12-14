package com.example.crud.board.domain;

import com.example.crud.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private int likesCnt = 0;

    private String tag;

    public void incrementLikes() {
        this.likesCnt++;
    }

    public void decrementLikes() {
        if (this.likesCnt > 0) {
            this.likesCnt--;
        }
    }
}
