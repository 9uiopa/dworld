package com.toy.dworld.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Article {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" , updatable = false)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content" , nullable = true)
    private String content;
    @Column(name = "author" , nullable = false)
    private String author;

    @Builder
    public Article(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    @CreatedDate
    @Column(name = "created")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated")
    private LocalDateTime updatedAt;

    public void update(String title, String content){
        this.title = title;
        this.content = content;

    }
}
