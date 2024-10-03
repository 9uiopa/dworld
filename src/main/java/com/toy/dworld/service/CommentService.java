package com.toy.dworld.service;

import com.toy.dworld.dto.AddCommentRequest;
import com.toy.dworld.entity.Article;
import com.toy.dworld.entity.Comment;
import com.toy.dworld.entity.User;
import com.toy.dworld.repo.ArticleRepository;
import com.toy.dworld.repo.CommentRepository;
import com.toy.dworld.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public List<Comment> getCommentsByArticleId(Long articleId){
        return commentRepository.findByArticleIdAndParentCommentIsNull(articleId);
    }

    public Comment addComment(long articleId, AddCommentRequest request) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new RuntimeException("article not found while adding comments"));
        User user = userRepository.findByEmail(request.getAuthor()).orElseThrow(() -> new RuntimeException("user not found while adding comments"));
        Long ParentCommentId = request.getParentCommentId();
        Comment comment;
        if(ParentCommentId==null){
            comment = request.toEntity(article, user);
        }else{
            Comment parentComment = commentRepository.findById(ParentCommentId).get();
            comment = request.toEntity(article, user,parentComment);
        }
        commentRepository.save(comment);
        return comment;
    }

    public int countComments(long articleId){
        return commentRepository.countByArticleId(articleId);
    }
}
