package com.toy.dworld.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data // 자동으로 getter, setter, equals, hashCode, toString 등의 메서드를 생성
@Entity
@Table(name = "board_type")
public class BoardType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

}
