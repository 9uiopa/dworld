package com.toy.dworld.controller;


import com.toy.dworld.dto.AddVoteRequest;
import com.toy.dworld.entity.Vote;
import com.toy.dworld.service.ArticleService;
import com.toy.dworld.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class VoteApiController {
    private final VoteService voteService;
    private final ArticleService articleService;

    @PostMapping("/api/articles/{id}/vote")
    public ResponseEntity<String> addVote(@RequestBody @Validated AddVoteRequest request,
                                          @PathVariable Long id,
                                          @AuthenticationPrincipal OAuth2User oauth2User) {
        // 로그인 여부 확인
        if (oauth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        String voterEmail = getUserEmail(oauth2User);

        if (hasUserAlreadyVoted(id, voterEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User has already voted.");
        }

        // 새로운 추천
        Vote newVote = voteService.addVote(request, id, voterEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVote.toString());
    }

    @GetMapping("/api/articles/{id}/upvotes")
    public Long countUpvotesForArticle(@PathVariable Long id){
        return voteService.countUpvotesForArticle(id);
    }

    @GetMapping("/api/articles/{id}/downvotes")
    public Long countDownvotesForArticle(@PathVariable Long id){
        return voteService.countDownvotesForArticle(id);
    }

    // 사용자 이메일 추출
    private String getUserEmail(OAuth2User oauth2User) {
        Map<String, Object> kakaoAccount = oauth2User.getAttribute("kakao_account");
        assert kakaoAccount != null;
        return (String) kakaoAccount.get("email");
    }

    // 기존 추천 여부 확인
    private boolean hasUserAlreadyVoted(Long articleId, String voterEmail) {
        return voteService.findByArticleIdAndUserEmail(articleId, voterEmail).isPresent();
    }
}