package com.toy.dworld.controller;

import com.toy.dworld.dto.ArticleViewResponse;
import com.toy.dworld.entity.Article;
import com.toy.dworld.entity.ArticleIndex;
import com.toy.dworld.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Optional;

import static com.toy.dworld.Constants.PAGE_SIZE;

@RequiredArgsConstructor
@Controller
public class ArticleViewController {
    private final ArticleService articleService;

    @GetMapping("/articles")
    public String getArticles(Model model,
                              @RequestParam(name = "page", defaultValue = "1") int page) {
        Page<Article> articlePage = articleService.getArticles(page - 1, PAGE_SIZE); //Page : JPA에서 제공하는 페이징용 인터페이스. 관련 메서드 정의돼 있음.
        model.addAttribute("articlePage", articlePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", PAGE_SIZE);
        return "articles/articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id).orElseThrow();
        model.addAttribute("article", new ArticleViewResponse(article));

        return "articles/article";
    }

    @GetMapping(value = "/articles", params = "query")
    public String searchArticles(@RequestParam(name = "query") String keyword,
                                 @RequestParam(name = "page", defaultValue = "1") int page, Model model) throws IOException {
        Page<ArticleIndex> articlePage = articleService.searchArticles(keyword, page-1, PAGE_SIZE);

        model.addAttribute("articlePage", articlePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", PAGE_SIZE);
        model.addAttribute("keyword", keyword);  // keyword를 모델에 추가
        return "articles/searchResult";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Optional<Article> article = articleService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article.orElseThrow()));
        }
        return "articles/newArticle";
    }

}
