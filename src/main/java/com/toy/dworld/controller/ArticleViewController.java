package com.toy.dworld.controller;

import com.toy.dworld.dto.ArticleViewResponse;
import com.toy.dworld.entity.Article;
import com.toy.dworld.entity.ArticleIndex;
import com.toy.dworld.entity.BoardType;
import com.toy.dworld.service.ArticleService;
import com.toy.dworld.service.BoardTypeService;
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
    private final BoardTypeService boardTypeService;
    @GetMapping(value = "/articles")
    public String getArticlesByBoardType(@RequestParam(name = "boardType", defaultValue = "1") Long boardType,
                                        @RequestParam(name = "page", defaultValue = "1") int page, Model model) throws IOException{
        Page<Article> articlePage = articleService.getArticlesByBoardType(boardType,page - 1, PAGE_SIZE); //Page : JPA에서 제공하는 페이징용 인터페이스. 관련 메서드 정의돼 있음.
        model.addAttribute("articlePage", articlePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", PAGE_SIZE);
        model.addAttribute("boardType",boardType);

        if (boardType == 1){
            return "articles/hotArticleList";
        }else{
            return "articles/articleList";
        }
    }

//    @GetMapping("/articles")
//    public String getArticles(Model model,
//                              @RequestParam(name = "page", defaultValue = "1") int page) {
//        Page<Article> articlePage = articleService.getArticles(page - 1, PAGE_SIZE); //Page : JPA에서 제공하는 페이징용 인터페이스. 관련 메서드 정의돼 있음.
//        model.addAttribute("articlePage", articlePage);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("pageSize", PAGE_SIZE);
//        return "articles/articleList";
//    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id).orElseThrow();
        model.addAttribute("article", new ArticleViewResponse(article));
        String boardTypeName = article.getBoardType().getName();
        model.addAttribute("boardType",boardTypeName);
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
    public String articleForm(@RequestParam(required = false) Long id,
                             @RequestParam(name = "boardType", required = true) Long boardTypeId  ,Model model) {
        if (id == null) { // 새 게시글
            BoardType boardType = boardTypeService.findById(boardTypeId).orElseThrow();
            model.addAttribute("boardName", boardType.getName());
            model.addAttribute("boardTypeId", boardTypeId);
            model.addAttribute("article", new ArticleViewResponse());
        } else { // 게시글 수정
            Optional<Article> article = articleService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article.orElseThrow()));

        }
        return "articles/newArticle";
    }

}
