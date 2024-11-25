package com.toy.dworld.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
// 게시물 상세 조회용
public class ArticleDetailDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String createdAt;
    private Long boardTypeId;
    private String boardTypeName;
    private List<CommentDTO> comments;
    private int upvotes;
    private int downvotes;
}
