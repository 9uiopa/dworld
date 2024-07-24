package com.toy.dworld.controller;


import com.toy.dworld.entity.Article;
import com.toy.dworld.dto.AddArticleRequest;
import com.toy.dworld.dto.ArticleResponse;
import com.toy.dworld.dto.UpdateArticleRequest;
import com.toy.dworld.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@RequestMapping("/api/articles")
@RequiredArgsConstructor
@RestController
public class ArticleApiController {
    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<Article> addArticle(@RequestBody @Validated AddArticleRequest request) throws IOException {
        Article newArticle = articleService.save(request, "오유리");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newArticle);
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
//    @GetMapping(params = "query")
//    public List<ArticleIndex> searchArticles(@RequestParam(name = "query") String keyword) throws IOException {
//        SearchResponse<ArticleIndex> searchResponse = articleService.searchArticles(keyword);
//        List<Hit<ArticleIndex>> listOfHits = searchResponse.hits().hits(); // hit : 검색 결과
//
//        return listOfHits.stream()
//                .map(hit -> {
//                    ArticleIndex article = hit.source(); // 기존 ArticleIndex 객체
//                    Objects.requireNonNull(article).setId(Long.parseLong(hit.id())); // Hit에서 ID 값을 설정
//                    return article;
//                })
//                .toList();
//    }



}
