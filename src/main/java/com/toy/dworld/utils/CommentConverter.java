package com.toy.dworld.utils;

import com.toy.dworld.dto.CommentDTO;
import com.toy.dworld.entity.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {
    public static CommentDTO toDTO(Comment comment) {
        // 부모 댓글이 있을 때 parentCommentId 설정
        Long parentCommentId = (comment.getParentComment() != null) ? comment.getParentComment().getId() : null;

        // 자식 댓글을 재귀적으로 DTO로 변환
        List<CommentDTO> childCommentDTOs = comment.getChildComments()
                .stream()
                .map(CommentConverter::toDTO) // 자식 댓글도 DTO로 변환
                .collect(Collectors.toList());

        return CommentDTO.builder()
                .id(comment.getId())
                .author(comment.getUser().getEmail()) // User에서 email만 가져옴
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .parentCommentId(parentCommentId) // 부모 댓글 ID
                .childComments(childCommentDTOs) // 자식 댓글 DTO 리스트
                .build();
    }
}
