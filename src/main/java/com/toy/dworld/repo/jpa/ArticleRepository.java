package com.toy.dworld.repo.jpa;

import com.toy.dworld.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
}
