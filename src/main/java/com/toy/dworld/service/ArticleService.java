package com.toy.dworld.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.toy.dworld.entity.Article;
import com.toy.dworld.dto.AddArticleRequest;
import com.toy.dworld.dto.UpdateArticleRequest;
import com.toy.dworld.entity.ArticleIndex;
import com.toy.dworld.repo.ArticleIndexRepository;
import com.toy.dworld.repo.jpa.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleIndexRepository articleIndexRepository;
    private final ElasticsearchClient elasticsearchClient;

    public Article save(AddArticleRequest request, String userName) throws IOException {
        //es index에 document 저장
        elasticsearchClient.index(i -> i
                .index("article")
                .document(request.toDocument(userName))
                .refresh(co.elastic.clients.elasticsearch._types.Refresh.True));
        // jpa 레코드 삽입
        return articleRepository.save(request.toEntity(userName));
    }

    public List<Article> findAll(){
        return articleRepository.findAll();
    }

    public Optional<Article> findById(long id) {
        return articleRepository.findById(id);
    }

    public void delete(long id) {
        articleRepository.deleteById(id);
    }

    @Transactional //트랜잭션 시작, 메서드 종료되면 트랜잭션이 커밋 or 롤백됨 - 일관성과 무결성을 유지
    public Article update(long id, UpdateArticleRequest request){
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        article.update(request.getTitle(), request.getContent());
        return article;
    }

    public SearchResponse<ArticleIndex> searchArticles(String keyword) throws IOException {
        // 쿼리 생성
        Query query = Query.of(q ->
                q.multiMatch(mmq -> mmq
                        .fields(Arrays.asList("title", "content"))
                        .query(keyword)
                        .fuzziness("AUTO")  // 자동으로 fuzziness 레벨 설정 - 문자열 길이가 길수록 허용 오차수가 늘어남
                )
        );
        //요청 생성
        SearchRequest request = SearchRequest.of(sr ->
                sr.index("article")
                        .query(query)
        );

        return elasticsearchClient.search(request, ArticleIndex.class);
    }


}
