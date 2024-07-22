package com.toy.dworld.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data // 자동으로 getter, setter, equals, hashCode, toString 등의 메서드를 생성
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" , updatable = false)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content" , nullable = true)
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "board_type_id", nullable = false)
    private BoardType boardType;
    @CreatedDate
    @Column(name = "created")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated")
    private LocalDateTime updatedAt;
    @Column(nullable = false)
    private boolean enabled = true;

    @Builder
    public Article(String title, String content, User user, BoardType boardType){
        this.title = title;
        this.content = content;
        this.user = user;
        this.boardType = boardType;
    }
    public void update(String title, String content){
        this.title = title;
        this.content = content;

    }
}
