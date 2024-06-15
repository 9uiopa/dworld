//package com.toy.dworld.controller;
//
//import com.toy.dworld.entity.Article;
//import com.toy.dworld.service.ArticleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RequiredArgsConstructor
//@RestController
//public class testController {
//    private final ArticleService articleService;
//
//
//    // db 연결 테스트 api
//    @GetMapping("/test/{author}")
//    public void addTestArticle(@PathVariable String author){
//        Article article = Article.builder()
//                .title("제목")
//                .content("test content")
//                .author(author)
//                .build();
//        articleService.saveTest(article);
//    }
//}
