package com.toy.dworld.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ArticleDetailDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String boardTypeName;
    private List<CommentDTO> comments;

}
