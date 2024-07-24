package com.toy.dworld.controller;

import com.toy.dworld.dto.AddCommentRequest;
import com.toy.dworld.entity.Comment;
import com.toy.dworld.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/articles/{articleId}/comments")
@RequiredArgsConstructor
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable long articleId, @RequestBody @Validated AddCommentRequest request){
        Comment addedComment = commentService.addComment(articleId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedComment);
    }

}
