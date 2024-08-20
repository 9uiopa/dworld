package com.toy.dworld.service;

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

    @Transactional
    public Vote addVote(AddVoteRequest request, Long articleId, String voterEmail){
        User user = userRepository.findByEmail(voterEmail).orElseThrow(()->
                        new IllegalArgumentException("User not found with email: " + voterEmail));
        Article article = articleRepository.findById(articleId).orElseThrow();
        Vote vote = request.toEntity(user, article, request.getVoteType());
        try {
            return voteRepository.save(vote);
        } catch (DataIntegrityViolationException ex) { // SQLIntegrityConstraintViolationException에 대한 Spring Exception
            // 데이터 무결성 위반 예외 처리 : 중복 추천 방지(유니크 키)
            throw new DataIntegrityViolationException("중복 추천은 불가능", ex);
        }

    }

    public Optional<Vote> findByArticleIdAndUserEmail(Long articleId, String email){
        return voteRepository.findByArticleIdAndUserEmail(articleId, email);
    }

}
