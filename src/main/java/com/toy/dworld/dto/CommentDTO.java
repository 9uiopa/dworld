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
    private Long parentCommentId; // 부모 댓글의 ID를 참조

}
