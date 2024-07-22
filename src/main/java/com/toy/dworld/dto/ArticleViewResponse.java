package com.toy.dworld.dto;

import com.toy.dworld.entity.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleViewResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    public ArticleViewResponse(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getUser().getUsername();
    }
}
