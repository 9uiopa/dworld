package com.toy.dworld.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.toy.dworld.domain.Article;
import com.toy.dworld.util.ElasticSearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class ElasticSearchService {
    private final ElasticsearchClient elasticsearchClient;

//    public SearchResponse<Article> matchArticlesWithKeyword(String fieldValue) throws IOException {
//        Supplier<Query> supplier = ElasticSearchUtil.supplierWithKeyword(fieldValue); // 쿼리문 생성
//        return elasticsearchClient.search(s->s.index("article").query(supplier.get()), Article.class);
//    }

    public SearchResponse<Article> matchArticlesWithKeyword(String fieldValue) throws IOException {
        Query query = Query.of(q -> q.multiMatch(mmq -> mmq.fields(Arrays.asList("title", "content")).query(fieldValue)));
        return elasticsearchClient.search(s -> s.index("article").query(query), Article.class);
    }




}
