package com.toy.dworld.repo;

import com.toy.dworld.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findByArticleIdAndUserEmail(Long articleId, String email);



}
