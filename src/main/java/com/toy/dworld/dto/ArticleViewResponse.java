package com.toy.dworld.dto;

import com.toy.dworld.entity.Article;
import com.toy.dworld.utils.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
// comment 정보없이 간단한 article DTO
public class ArticleViewResponse implements Serializable{
    private Long id;
    private String title;
    private String content;
    private String author;
    private String createdAt;
    private int commentCount = 0;

    public ArticleViewResponse(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getUser().getEmail();
        this.createdAt = DateUtils.formatLocalDateTime(article.getCreatedAt());
    }
}
