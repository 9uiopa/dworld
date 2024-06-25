package com.toy.dworld.controller;

import com.toy.dworld.dto.ArticleListViewResponse;
import com.toy.dworld.dto.ArticleViewResponse;
import com.toy.dworld.entity.Article;
import com.toy.dworld.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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

}
