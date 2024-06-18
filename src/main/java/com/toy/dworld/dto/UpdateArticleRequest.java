package com.toy.dworld.dto;

import com.toy.dworld.entity.ArticleIndex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateArticleRequest {
    private String title;
    private String content;
    private String author;
    public ArticleIndex toDocument(){
        return ArticleIndex.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
