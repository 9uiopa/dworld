package com.toy.dworld.dto;

import com.toy.dworld.entity.Comment;
import com.toy.dworld.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private String author;
    private String createdAt;
    private List<CommentDTO> childComments;
    private Long parentCommentId;
    private boolean enabled;

    public CommentDTO(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getUser().getEmail();
        this.createdAt = DateUtils.formatLocalDateTime(comment.getCreatedAt());
        this.childComments = comment.getChildComments().stream().map(CommentDTO::new).toList();
        this.parentCommentId = Optional.ofNullable(comment.getParentComment())
                .map(Comment::getId)
                .orElse(null);
        this.enabled = comment.isEnabled();
    }

}
