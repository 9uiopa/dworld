package com.toy.dworld.controller;

import com.toy.dworld.dto.*;
import com.toy.dworld.service.ArticleService;
import com.toy.dworld.service.BoardTypeService;
import com.toy.dworld.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.util.List;

import static com.toy.dworld.Constants.PAGE_SIZE;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ArticleViewController {
    private final ArticleService articleService;
    private final BoardTypeService boardTypeService;

    @GetMapping(value = "/articles")
    public String getArticlesByBoardType(@RequestParam(name = "boardType", defaultValue = "1") Long boardType,
                                        @RequestParam(name = "page", defaultValue = "1") int page, Model model) throws IOException{
        model.addAttribute("boardType",boardType);

        if (boardType == 1){
            // 인기 게시판
            Page<ArticleViewResponse> hotArticles = articleService.getHotArticles(page - 1, PAGE_SIZE);
            model.addAttribute("articlePage",hotArticles);
            return "articles/hotArticleList";
        }else{
            // 다른 게시판
            Page<ArticleViewResponse> articlePage = articleService.getArticlesByBoardType(boardType,page - 1, PAGE_SIZE); //Page : JPA에서 제공하는 페이징용 인터페이스. 관련 메서드 정의돼 있음.
            model.addAttribute("articlePage", articlePage);

            return "articles/articleList";
        }
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        model.addAttribute("article", articleService.getArticleDetail(id));
        return "articles/article";
    }

    @GetMapping(value = "/articles", params = "query")
    public String searchArticles(@RequestParam(name = "query") String keyword,
                                 @RequestParam(name = "page", defaultValue = "1") int page, Model model) throws IOException {
        Page<ArticleIndexDTO> articlePage = articleService.searchArticles(keyword, page-1, PAGE_SIZE);

        model.addAttribute("articlePage", articlePage);
        model.addAttribute("keyword", keyword);
        return "articles/searchResult";
    }

    @GetMapping("/new-article")
    public String articleForm(@RequestParam(required = false) Long id,
                             @RequestParam(name = "boardType", required = true) Long boardTypeId  ,Model model) {
        BoardTypeDTO boardType = boardTypeService.findById(boardTypeId);
        model.addAttribute("boardType", boardType);
        if (id == null) { // 새 게시글 작성
            model.addAttribute("article", new ArticleViewResponse());
        } else { // 게시글 수정
           ArticleViewResponse article = new ArticleViewResponse(articleService.findById(id)
                   .orElseThrow(()-> new IllegalArgumentException("해당 id에 맞는 게시물이 없음. id:" + id)));
            model.addAttribute("article", article);
        }
        return "articles/newArticle";
    }


}
