package com.toy.dworld.repo;

import com.toy.dworld.entity.ArticleIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
public interface ArticleIndexRepository extends ElasticsearchRepository<ArticleIndex,Long> {

}

