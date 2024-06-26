package com.toy.dworld.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.toy.dworld.dto.ArticleListViewResponse;
import com.toy.dworld.dto.ArticleViewResponse;
import com.toy.dworld.entity.Article;
import com.toy.dworld.entity.ArticleIndex;
import com.toy.dworld.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class ArticleViewController {
    private final ArticleService articleService;

    @GetMapping("/articles")
    public String getArticles(Model model){
        List<ArticleListViewResponse> articles = articleService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles",articles);
        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model){
        Article article = articleService.findById(id).orElseThrow();
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    @GetMapping(value = "/articles",params = "query")
    public String searchArticles(@RequestParam(name = "query") String keyword, Model model) throws IOException {
        SearchResponse<ArticleIndex> searchResponse = articleService.searchArticles(keyword);
        List<Hit<ArticleIndex>> listOfHits = searchResponse.hits().hits(); // hit : 검색 결과
        List<ArticleIndex> articles = listOfHits.stream()
                .map(hit -> {
                    ArticleIndex article = hit.source(); // 기존 ArticleIndex 객체
                    Objects.requireNonNull(article).setId(Long.parseLong(hit.id())); // Hit에서 ID 값을 설정
                    return article;
                })
                .toList();
        model.addAttribute("articles",articles);
        return "articleList";
    }

}
