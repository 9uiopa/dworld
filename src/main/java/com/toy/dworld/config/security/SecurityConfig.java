package com.toy.dworld.config.security;

import com.toy.dworld.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final PreviousPageInterceptor previousPageInterceptor;

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
                                .successHandler(customAuthenticationSuccessHandler)  // 커스텀 성공 핸들러 적용
                                .failureUrl("/login?error=true")
                                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        )
                        .build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 이전 페이지 저장을 위한 인터셉터 등록
        registry.addInterceptor(previousPageInterceptor);
    }
}
