package com.toy.dworld.repo;

import com.toy.dworld.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByArticleId(Long articleId);
    List<Comment> findByArticleIdAndParentCommentIsNull(Long articleId);
}
