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
import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
public class AddArticleRequest {
    @NotBlank
    @Size(min=1, max = 15)
    private String title;
    @NotNull
    private String content;
    private String email;
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

    public ArticleIndex toDocument(String email){
        return ArticleIndex.builder()
                .title(title)
                .content(content)
                .email(email)
                .boardTypeId(boardTypeId)
                .build();
    }
}
