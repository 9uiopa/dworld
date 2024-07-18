package com.toy.dworld.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.toy.dworld.entity.Article;
import com.toy.dworld.dto.AddArticleRequest;
import com.toy.dworld.dto.UpdateArticleRequest;
import com.toy.dworld.entity.ArticleIndex;
import com.toy.dworld.repo.ArticleIndexRepository;
import com.toy.dworld.repo.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleIndexRepository articleIndexRepository;
    private final ElasticsearchClient elasticsearchClient;

    public Article save(AddArticleRequest request, String userName) throws IOException {
        // jpa 레코드 삽입
        Article newArticle = articleRepository.save(request.toEntity(userName));

        //es index에 document 저장
        elasticsearchClient.index(i -> i
                .index("article")
                .document(request.toDocument(userName))
                .id(newArticle.getId().toString())
                .refresh(co.elastic.clients.elasticsearch._types.Refresh.True));
        // jpa 레코드 삽입
        return newArticle;
    }

    public Page<Article> getArticlesByBoardType(long boardTypeId,int page, int size) {

        return articleRepository.findByBoardTypeId(boardTypeId,PageRequest.of(page, size));

    }

    public Optional<Article> findById(long id) {
        return articleRepository.findById(id);
    }

    public void delete(long id) throws IOException {
        elasticsearchClient.delete(d -> d
                .index("article")
                .id(String.valueOf(id))
                .refresh(co.elastic.clients.elasticsearch._types.Refresh.True));

        articleRepository.deleteById(id);
    }

    @Transactional //트랜잭션 시작, 메서드 종료되면 트랜잭션이 커밋 or 롤백됨 - 일관성과 무결성을 유지
    public Article update(long id, UpdateArticleRequest request) throws IOException {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        article.update(request.getTitle(), request.getContent());

        elasticsearchClient.index(i -> i
                .index("article")
                .id(String.valueOf(id))
                .document(request.toDocument())
                .refresh(co.elastic.clients.elasticsearch._types.Refresh.True));

        return article;
    }

    public Page<ArticleIndex> searchArticles(String keyword, int page, int size) throws IOException {
        Pageable pageable = PageRequest.of(page, size);
        // 쿼리 생성
        Query query = Query.of(q -> q.multiMatch(mmq -> mmq
                        .fields(Arrays.asList("title", "content"))
                        .query(keyword)
                        .fuzziness("AUTO")  // 자동으로 fuzziness 레벨 설정
                )
        );
        // 로그 출력
        System.out.println("Elasticsearch Query: " + query.toString());

        // 요청 생성
        SearchRequest request = SearchRequest.of(sr -> sr.index("article")
                .query(query)
                .from(page)  // 시작점 설정
                .size(size)  // 페이지 크기 설정
        );
        // 로그 출력
        System.out.println("SearchRequest: " + request.toString());


        //elastic search - 쿼리 수행
        SearchResponse<ArticleIndex> searchResponse = elasticsearchClient.search(request, ArticleIndex.class);
        List<Hit<ArticleIndex>> listOfHits = searchResponse.hits().hits();

        List<ArticleIndex> articles = listOfHits.stream()
                .map(hit -> {
                    ArticleIndex article = hit.source();
                    article.setId(Long.parseLong(hit.id()));
                    return article;
                })
                .collect(Collectors.toList());

        long totalHits = searchResponse.hits().total().value();

        // 로그 출력
        System.out.println("Total Hits: " + totalHits);
        System.out.println("Articles: " + articles);

        return new PageImpl<>(articles, pageable, totalHits);
    }


}
