package com.toy.dworld.dto;

import com.toy.dworld.entity.ArticleIndex;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ArticleIndexDTO{
    private Long id;
    private String title;
    private String content;
    private String email;
    private Long boardTypeId;
    private LocalDateTime createdAt;

    public  ArticleIndexDTO(ArticleIndex articleIndex){
        this.id = articleIndex.getId();
        this.title = articleIndex.getTitle();
        this.content=articleIndex.getContent();
        this.email = articleIndex.getEmail();
        this.boardTypeId = articleIndex.getBoardTypeId();
        this.createdAt = articleIndex.getCreatedAt();
    }
}
