package com.toy.dworld.dto;

import com.toy.dworld.entity.Article;
import com.toy.dworld.entity.ArticleIndex;
import com.toy.dworld.entity.BoardType;
import com.toy.dworld.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@NoArgsConstructor //  hibernate가 객체 생성해야하기 때문에 필요
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    @NotBlank
    @Size(min=1, max = 15)
    private String title;
    @NotNull
    private String content;
    @NotBlank
    private String author;
    @NotNull
    private Long boardTypeId;

    public Article toEntity(User user, BoardType boardType){ // DTO 이용해서 Entity 반환
        return Article.builder()
                .title(title)
                .content(content)
                .user(user)
                .boardType(boardType)
                .build();
    }

    public ArticleIndex toDocument(String username){
        return ArticleIndex.builder()
                .title(title)
                .content(content)
                .username(username)
                .boardTypeId(boardTypeId)
                .build();
    }
}
