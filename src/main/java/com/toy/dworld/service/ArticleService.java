//package com.toy.dworld.service;
//
//import com.toy.dworld.entity.Article;
//import com.toy.dworld.dto.AddArticleRequest;
//import com.toy.dworld.dto.UpdateArticleRequest;
//import com.toy.dworld.repo.jpa.ArticleRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Service
//public class ArticleService {
//    private final ArticleRepository articleRepository;
//
//    public Article save(AddArticleRequest request, String userName){
//        return articleRepository.save(request.toEntity(userName));
//    }
//
//    //db 연결 확인용
//    public void saveTest(Article article){
//        articleRepository.save(article);
//    }
//
//    public List<Article> findAll(){
//        return articleRepository.findAll();
//    }
//
//    public Optional<Article> findById(long id) {
//        return articleRepository.findById(id);
//    }
//
//    public void delete(long id) {
//        articleRepository.deleteById(id);
//    }
//
//    @Transactional //트랜잭션 시작, 메서드 종료되면 트랜잭션이 커밋 or 롤백됨 - 일관성과 무결성을 유지
//    public Article update(long id, UpdateArticleRequest request){
//        Article article = articleRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
//        article.update(request.getTitle(), request.getContent());
//        return article;
//    }
//
//
//}
