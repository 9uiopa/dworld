package com.toy.dworld.dto;

import com.toy.dworld.entity.BoardType;
import lombok.Data;

@Data
public class BoardTypeDTO {
    private Long id;
    private String name;

    public BoardTypeDTO(BoardType boardType){
        this.id = boardType.getId();
        this.name = boardType.getName();
    }
}
