package com.toy.dworld.controller;


import com.toy.dworld.dto.ArticleViewResponse;
import com.toy.dworld.entity.Article;
import com.toy.dworld.dto.AddArticleRequest;
import com.toy.dworld.dto.UpdateArticleRequest;
import com.toy.dworld.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
@Slf4j
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@RestController
public class ArticleApiController {
    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleViewResponse> addArticle(@RequestBody @Validated AddArticleRequest request,
                                                          @AuthenticationPrincipal OAuth2User oauth2User) throws IOException {
        try {
            Map<String, Object> kakaoAccount = oauth2User.getAttribute("kakao_account");
            if (kakaoAccount == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            String email = (String) kakaoAccount.get("email");
            request.setEmail(email);
            ArticleViewResponse newArticle = articleService.save(request, email);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(newArticle);
        } catch (Exception e) {
            e.printStackTrace(); // 또는 로깅 라이브러리를 사용하여 로그를 남김
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving article", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleViewResponse> findArticle(@PathVariable long id){
        Optional<Article> article = articleService.findById(id);
        return article.map(a -> ResponseEntity.ok().body(new ArticleViewResponse(a)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found")); // optional이 비어 있을 때
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) throws IOException {
        articleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleViewResponse> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request) throws IOException {
        ArticleViewResponse updatedArticle = articleService.update(id, request);
        return ResponseEntity.ok().body(updatedArticle);
    }

}
