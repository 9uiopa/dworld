//package com.toy.dworld.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.toy.dworld.dto.AddArticleRequest;
//import com.toy.dworld.dto.ArticleViewResponse;
//import com.toy.dworld.repo.ArticleRepository;
//import com.toy.dworld.service.ArticleService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Collections;
//import java.util.Map;
//
//import static org.hamcrest.Matchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc // MockMvc 사용을 위해
//public class ArticleApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    protected ObjectMapper objectMapper;
//
//    @MockBean
//    private ArticleService articleService; // ArticleService를 MockBean으로 추가
//
//    @MockBean
//    private OAuth2User oauth2User;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }
//
//    @Test
//    public void addArticle() throws Exception {
//        // Given
//        AddArticleRequest request = new AddArticleRequest("Test Article", "This is a test article.", "test@example.com", 4L);
//        Map<String, Object> kakaoAccount = Map.of("email", "test@example.com");
//
//        // Mocking oauth2User
//        when(oauth2User.getAttribute("kakao_account")).thenReturn(kakaoAccount);
//        SecurityContextHolder.getContext().setAuthentication(new OAuth2AuthenticationToken(oauth2User, Collections.EMPTY_SET, "test-client-id"));
//
//        // Mocking articleService의 save 메서드
//        ArticleViewResponse response = new ArticleViewResponse();
//        response.setTitle("Test Article");
//        response.setContent("This is a test article.");
//        given(articleService.save(request,"test@example.com")).willReturn(response);
//
//        // When & Then
//        mockMvc.perform(post("/api/articles").with(oauth2Login())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())  // HTTP 201 Created 기대
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.title").value("Test Article"))
//                .andExpect(jsonPath("$.content").value("This is a test article."));
//    }
//
//
//}
