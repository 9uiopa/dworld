package com.toy.dworld.dto;

import com.toy.dworld.entity.Article;
import com.toy.dworld.entity.ArticleIndex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@NoArgsConstructor //  hibernate가 객체 생성해야하기 때문에 필요
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    @NotNull
    @Size(min=1, max = 15)
    private String title;

    @NotNull
    private String content;
    @NotNull
    private String author;
    @NotNull
    private Long boardType_id;

    public Article toEntity(String author){
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .boardTypeId(boardType_id)
                .build();
    }

    public ArticleIndex toDocument(String author){
        return ArticleIndex.builder()
                .title(title)
                .content(content)
                .author(author)
                .boardTypeId(boardType_id)
                .build();
    }
}
