package com.toy.dworld.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.toy.dworld.entity.Article;
import com.toy.dworld.dto.AddArticleRequest;
import com.toy.dworld.dto.ArticleResponse;
import com.toy.dworld.dto.UpdateArticleRequest;
import com.toy.dworld.entity.ArticleIndex;
import com.toy.dworld.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/articles")
@RequiredArgsConstructor
@RestController
public class ArticleApiController {
    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<Article> addArticle(@RequestBody @Validated AddArticleRequest request) throws IOException {
        Article newArticle = articleService.save(request, "author");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newArticle);
    }


    @GetMapping
    public ResponseEntity<List<ArticleResponse>> findAllArticles(){
        List<ArticleResponse> articles = articleService.findAll()
                .stream()
                .map(ArticleResponse::new) // from List<Article> to List<ArticleResponse>
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id){
        Optional<Article> article = articleService.findById(id);
        return article.map(a -> ResponseEntity.ok().body(new ArticleResponse(a)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found")); // optional이 비어 있을 때
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) throws IOException {
        articleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request) throws IOException {
        Article updatedArticle = articleService.update(id, request);
        return ResponseEntity.ok().body(updatedArticle);
    }

    // 게시물 검색
    @GetMapping(params = "keyword")
    public List<ArticleIndex> searchArticles(@RequestParam String keyword) throws IOException {
        SearchResponse<ArticleIndex> searchResponse = articleService.searchArticles(keyword);
        List<Hit<ArticleIndex>> listOfHits = searchResponse.hits().hits(); // hit : 검색 결과

        return listOfHits.stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }



}
