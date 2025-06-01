package com.toy.dworld.service;

import com.toy.dworld.Constants;
import com.toy.dworld.dto.AddVoteRequest;
import com.toy.dworld.entity.Article;
import com.toy.dworld.entity.User;
import com.toy.dworld.entity.Vote;
import com.toy.dworld.repo.ArticleRepository;
import com.toy.dworld.repo.UserRepository;
import com.toy.dworld.repo.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final org.springframework.context.ApplicationContext applicationContext;

    @Transactional
    public Vote addVote(AddVoteRequest request, Long articleId, String voterEmail) {
        User user = findUserByEmail(voterEmail);
        Article article = findArticleById(articleId);
        Vote vote = createVote(request, user, article);

        Vote newVote = saveVote(vote);
        updateArticleVoteCount(article,vote);

        return newVote;
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id: " + articleId));
    }

    private Vote createVote(AddVoteRequest request, User user, Article article) {
        return request.toEntity(user, article, request.getVoteType());
    }

    private Vote saveVote(Vote vote) {
        try {
            return voteRepository.save(vote);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("중복 추천은 불가능", ex);
        }
    }
    // 게시물의 추천수 count
    private void updateArticleVoteCount(Article article, Vote vote) {
        //추천일 때
        if (vote.getVoteType() == Vote.VoteType.UPVOTE) {
            article.setUpvotes(article.getUpvotes()+1);
            articleRepository.save(article);
            // 인기글 커트라인 넘었을 때 인기게시글 캐시 무효화
            if(article.getUpvotes()+1 >= Constants.HOT_ARTICLE_THRESHOLD){
                // (VoteService를 구현 또는 상속 중인)프록시 객체를 참조해서 메서드 내부호출문제 해결
                VoteService proxy = applicationContext.getBean(VoteService.class);
                proxy.evictHotArticleCache();
            }
        //비추천일 때
        } else if (vote.getVoteType() == Vote.VoteType.DOWNVOTE) {
            article.setDownvotes(article.getDownvotes()+1);
            articleRepository.save(article);
        }
    }
    @CacheEvict(value = "hotArticles", allEntries = true) // hotArticles 캐시 내의 모든 항목을 삭제
    public void evictHotArticleCache(){
        log.debug("cacheEvicted : hot");
    }
    public Optional<Vote> findByArticleIdAndUserEmail(Long articleId, String email) {
        return voteRepository.findByArticleIdAndUserEmail(articleId, email);
    }

    public Long countUpvotesForArticle(Long articleId) {
        return voteRepository.countUpvotesForArticle(articleId);
    }

    public Long countDownvotesForArticle(Long articleId) {
        return voteRepository.countDownvotesForArticle(articleId);
    }


}
