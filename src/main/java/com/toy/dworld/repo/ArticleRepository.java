package com.toy.dworld.repo;

import com.toy.dworld.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    Page<Article> findByBoardTypeId(Long boardTypeId, Pageable pageable);
    Page<Article> findByUpvotesGreaterThanEqual(int minUpvotes,Pageable pageable);
}
