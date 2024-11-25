package com.toy.dworld.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.toy.dworld.dto.*;
import com.toy.dworld.entity.Article;
import com.toy.dworld.entity.ArticleIndex;
import com.toy.dworld.entity.BoardType;
import com.toy.dworld.entity.User;
import com.toy.dworld.repo.ArticleRepository;
import com.toy.dworld.repo.BoardTypeRepository;
import com.toy.dworld.repo.UserRepository;
import com.toy.dworld.utils.CommentConverter;
import com.toy.dworld.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.toy.dworld.Constants.HOT_ARTICLE_THRESHOLD;
import static org.springframework.data.domain.Sort.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final UserRepository userRepository;
    private final BoardTypeRepository boardTypeRepository;
    private final CommentService commentService;

    public ArticleViewResponse save(AddArticleRequest request, String email) throws IOException {
        // db 저장
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        BoardType boardType = boardTypeRepository.findById(request.getBoardTypeId())
                .orElseThrow(() -> new RuntimeException("Board type not found"));
        ArticleViewResponse newArticle = new ArticleViewResponse(articleRepository.save(request.toEntity(user,boardType)));

        //elasticsearch index: document 저장
        elasticsearchClient.index(i -> i
                .index("article")
                .document(request.toDocument())
                .id(newArticle.getId().toString())
                .refresh(co.elastic.clients.elasticsearch._types.Refresh.True));
        return newArticle;
    }

    public Page<ArticleViewResponse> getArticlesByBoardType(long boardTypeId,int page, int size) {
        Page<Article> articles = articleRepository.findByBoardTypeId(
                boardTypeId,
                PageRequest.of(page, size, by(DESC, "createdAt")));
        return toArticleViewResponsePage(articles);
    }

    // Article -> ArticleViewResponse 변환 및 commentCount 설정
    @NotNull
    private Page<ArticleViewResponse> toArticleViewResponsePage(Page<Article> articles) {
        List<ArticleViewResponse> articleViewResponses = articles.stream()
                .map(article -> {
                    ArticleViewResponse response = new ArticleViewResponse(article);
                    response.setCommentCount(commentService.countComments(article.getId()));
                    return response;
                })
                .toList();
        return new PageImpl<>(articleViewResponses, articles.getPageable(), articles.getTotalElements());
    }

    @Cacheable(value = "hotArticles", key = "'hot_' + #page + '_' + #size") //value : 캐시이름 key : 키 , key의 value : 메소드 반환값
    public Page<ArticleViewResponse> getHotArticles(int page, int size){
        Page<Article> articles = articleRepository.findByUpvotesGreaterThanEqual(
                HOT_ARTICLE_THRESHOLD,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return toArticleViewResponsePage(articles);
    }

    public ArticleDetailDTO getArticleDetail(long id){
        Article article = findById(id).orElseThrow(() -> new IllegalArgumentException(id + "라는 id를 가진 게시물이 없습니다."));
        List<CommentDTO> commentDTOList = commentService.getCommentsByArticleId(id)
                .stream()
                .map(CommentConverter::toDTO)
                .toList();
        return ArticleDetailDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .author(article.getUser().getEmail())
                .content(article.getContent())
                .createdAt(DateUtils.formatLocalDateTime(article.getCreatedAt()))
                .comments(commentDTOList)
                .boardTypeId(article.getBoardType().getId())
                .boardTypeName(article.getBoardType().getName())
                .upvotes(article.getUpvotes())
                .downvotes(article.getDownvotes())
                .build();
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
    public ArticleViewResponse update(long id, UpdateArticleRequest request) throws IOException {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        article.update(request.getTitle(), request.getContent());

        elasticsearchClient.index(i -> i
                .index("article")
                .id(String.valueOf(id))
                .document(request.toDocument())
                .refresh(co.elastic.clients.elasticsearch._types.Refresh.True));

        return new ArticleViewResponse(article);
    }

    public Page<ArticleIndexDTO> searchArticles(String keyword, int page, int size) throws IOException {
        Pageable pageable = PageRequest.of(page, size,by(DESC,"createdAt"));
        // 쿼리 생성
        Query query = Query.of(q -> q.multiMatch(mmq -> mmq
                        .fields(Arrays.asList("title", "content"))
                        .query(keyword)
                        .fuzziness("AUTO")  // 자동으로 fuzziness 레벨 설정
                )
        );

        // 요청 생성
        SearchRequest request = SearchRequest.of(sr -> sr.index("article")
                .query(query)
                .from(page)  // 시작점 설정
                .size(size)  // 페이지 크기 설정
        );

        //elastic search - 쿼리 수행
        SearchResponse<ArticleIndex> searchResponse = elasticsearchClient.search(request, ArticleIndex.class);
        List<Hit<ArticleIndex>> listOfHits = searchResponse.hits().hits();

        List<ArticleIndexDTO> articles = listOfHits.stream()
                .map(hit -> {
                    ArticleIndex article = hit.source();
                    article.setId(Long.parseLong(hit.id()));
                    return new ArticleIndexDTO(article);
                })
                .collect(Collectors.toList());

        long totalHits = searchResponse.hits().total().value();

        return new PageImpl<>(articles, pageable, totalHits);
    }


}
