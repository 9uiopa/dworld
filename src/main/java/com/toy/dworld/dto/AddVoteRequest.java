package com.toy.dworld.dto;

import com.toy.dworld.entity.Article;
import com.toy.dworld.entity.User;
import com.toy.dworld.entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddVoteRequest {

    @NotNull
    private Vote.VoteType voteType;

    public Vote toEntity(User user, Article article, Vote.VoteType voteType){
        return Vote.builder()
                .user(user)
                .article(article)
                .voteType(voteType)
                .build();
    }
}
