package com.toy.dworld.repo;

import com.toy.dworld.entity.ArticleIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
public interface ArticleIndexRepository extends ElasticsearchRepository<ArticleIndex,Long> {
    public void save(ArticleIndex articleIndex);

}

