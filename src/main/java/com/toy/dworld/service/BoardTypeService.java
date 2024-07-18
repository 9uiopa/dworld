package com.toy.dworld.service;

import com.toy.dworld.entity.BoardType;
import com.toy.dworld.repo.BoardTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardTypeService {
    private final BoardTypeRepository boardTypeRepository;

    public List<BoardType> findAll(){
        return boardTypeRepository.findAll();
    }

    public Optional<BoardType> findById(Long id) {
        return boardTypeRepository.findById(id);
    }
}
