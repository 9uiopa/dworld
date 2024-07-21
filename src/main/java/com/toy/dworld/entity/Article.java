package com.toy.dworld.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;

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
    @Column(name = "author" , nullable = false)
    private String author;
    @Column(name = "board_type_id" , nullable = false)
    private Long boardTypeId;
    @CreatedDate
    @Column(name = "created")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated")
    private LocalDateTime updatedAt;

    @Builder
    public Article(String title, String content, String author,Long boardTypeId){
        this.title = title;
        this.content = content;
        this.author = author;
        this.boardTypeId = boardTypeId;
    }
    public void update(String title, String content){
        this.title = title;
        this.content = content;

    }
}
