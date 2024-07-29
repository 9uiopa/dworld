package com.toy.dworld.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false) // name : 컬럼 이름, 참조 컬럼은 참조하는 엔티티의 기본키(변경 가능)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id", nullable = true)
    @JsonBackReference // 자식 댓글을 직렬화할 때 부모 댓글의 참조를 무시합니다.
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true) // 부모 댓글 사라질 때 자식도 사라짐. 어차피 실제로 삭제 안하면 됨
    @JsonManagedReference
    private List<Comment> childComments = new ArrayList<>();

    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    @Builder
    public Comment(String content, Article article, User user,Comment parentComment){
        this.content = content;
        this.article = article;
        this.user = user;
        this.parentComment = parentComment;
    }
    public void update(String content){
        this.content = content;

    }
}
