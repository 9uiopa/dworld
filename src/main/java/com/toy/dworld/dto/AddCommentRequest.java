package com.toy.dworld.dto;

import com.toy.dworld.entity.Article;
import com.toy.dworld.entity.Comment;
import com.toy.dworld.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentRequest {
    @NotBlank
    private String content;
    @NotNull
    private String author;
    private Long ParentCommentId;

    public Comment toEntity(Article article, User user, Comment parentComment){
        return Comment.builder()
                .content(content)
                .user(user)
                .article(article)
                .parentComment(parentComment)
                .build();
    }
}
