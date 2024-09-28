package com.toy.dworld.dto;

import com.toy.dworld.entity.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
// comment 정보없이 간단한 article DTO
public class ArticleViewResponse implements Serializable {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public ArticleViewResponse(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getUser().getEmail();
        this.createdAt = article.getCreatedAt();
    }
}
