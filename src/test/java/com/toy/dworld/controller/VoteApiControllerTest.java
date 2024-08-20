package com.toy.dworld.controller;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.dworld.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(VoteApiController.class)
class VoteApiControllerTest {

    @InjectMocks
    private VoteApiController voteApiController;

    @Mock
    private VoteService voteService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(voteApiController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addVote() throws Exception {
    }
}