package com.toy.dworld.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data // 자동으로 getter, setter, equals, hashCode, toString 등의 메서드를 생성
@Document(indexName = "article")
@JsonIgnoreProperties(ignoreUnknown = true) // 기본으로 존재하는 _class 요소를 무시하기 위해서 사용
public class ArticleIndex {
    @Id
    private Long id;
    private String title;
    private String content;
    private String username;
    private Long boardTypeId;

}
