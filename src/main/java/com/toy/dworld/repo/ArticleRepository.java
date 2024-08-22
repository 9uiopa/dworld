package com.toy.dworld.repo;

import com.toy.dworld.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    Page<Article> findByBoardTypeId(Long boardTypeId, Pageable pageable);
    Page<Article> findByUpvotesGreaterThanEqual(int minUpvotes,Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Article a SET a.upvotes = a.upvotes + 1 WHERE a.id = :id")
    int incrementUpvotes(Long id); // update된 row 개수 반환

    @Transactional
    @Modifying
    @Query("UPDATE Article a SET a.downvotes = a.downvotes + 1 WHERE a.id = :id")
    int incrementDownvotes(Long id); // update된 row 개수 반환
}
