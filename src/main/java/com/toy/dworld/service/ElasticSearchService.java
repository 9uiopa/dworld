package com.toy.dworld.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.toy.dworld.entity.ArticleIndex;
import com.toy.dworld.util.ElasticSearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class ElasticSearchService {
    private final ElasticsearchClient elasticsearchClient;

    public SearchResponse<ArticleIndex> matchArticlesWithKeywords(String fieldValue) throws IOException {
        Supplier<Query> supplier = ElasticSearchUtil.supplierWithKeyword(fieldValue); // 쿼리문 생성
        return elasticsearchClient.search(s->s.index("article").query(supplier.get()), ArticleIndex.class);
    }

    public SearchResponse<ArticleIndex> matchArticlesWithKeyword(String fieldValue) throws IOException {
        Query query = Query.of(q -> q.multiMatch(mmq -> mmq.fields(Arrays.asList("title", "content")).query(fieldValue)));
        return elasticsearchClient.search(s -> s.index("article").query(query), ArticleIndex.class);
    }

    public SearchResponse<ArticleIndex> searchArticles(String keyword) throws IOException {
        // 쿼리 생성
        Query query = Query.of(q ->
                q.multiMatch(mmq -> mmq
                        .fields(Arrays.asList("title", "content"))
                        .query(keyword)
                        .fuzziness("AUTO")  // 자동으로 fuzziness 레벨 설정 - 문자열 길이가 길수록 허용 오차수가 늘어남
                )
        );
        //요청 생성
        SearchRequest request = SearchRequest.of(sr ->
                sr.index("article")
                        .query(query)
        );

        return elasticsearchClient.search(request, ArticleIndex.class);
    }

    public SearchResponse<Map> matchAllServices() throws IOException {
        Supplier<Query> supplier = ElasticSearchUtil.supplier();
        SearchResponse<Map> searchResponse = elasticsearchClient.search(s->s.query(supplier.get()), Map.class);
        return searchResponse;
    }



}
