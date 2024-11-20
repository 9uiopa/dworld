package com.toy.dworld.dto;

import com.toy.dworld.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private List<CommentDTO> childComments;
    private Long parentCommentId;
    private boolean enabled;

    public CommentDTO(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getUser().getEmail();
        this.createdAt = comment.getCreatedAt();
        this.childComments = comment.getChildComments().stream().map(CommentDTO::new).toList();
        this.parentCommentId = comment.getParentComment().getId();
        this.enabled = comment.isEnabled();
    }

}
