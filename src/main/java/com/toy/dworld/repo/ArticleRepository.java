package com.toy.dworld.repo;

import com.toy.dworld.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    Page<Article> findByBoardTypeId(Long boardTypeId, Pageable pageable);
}
