package com.toy.dworld.repo;

import com.toy.dworld.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findByArticleIdAndUserEmail(Long articleId, String email);
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.article.id = :articleId AND v.voteType = 'UPVOTE'")
    Long countUpvotesForArticle(Long articleId);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.article.id = :articleId AND v.voteType = 'DOWNVOTE'")
    Long countDownvotesForArticle(Long articleId);
}
