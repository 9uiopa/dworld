package com.toy.dworld.config;

import com.toy.dworld.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http
                        .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                .requestMatchers("/new-article").authenticated()
                                .anyRequest().permitAll()
                        )
                        .oauth2Login(oauth2Login -> oauth2Login
                                .loginPage("/login") // 로그인하지 않았을 때 리디렉션될 페이지
                                .defaultSuccessUrl("/articles", true)
                                .failureUrl("/login?error=true")
                                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        )
                        .build();
    }
}
