package com.toy.dworld.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.toy.dworld.entity.ArticleIndex;
import com.toy.dworld.service.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/articles")
@RequiredArgsConstructor
@RestController
public class EsIndexController {
    private final ElasticSearchService elasticSearchService;

        @GetMapping(params = "keyword")
    public List<ArticleIndex> searchArticles(@RequestParam String keyword) throws IOException {
        SearchResponse<ArticleIndex> searchResponse = elasticSearchService.searchArticles(keyword);
        List<Hit<ArticleIndex>> listOfHits = searchResponse.hits().hits(); // hit : 검색 결과

        return listOfHits.stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
