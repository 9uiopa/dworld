package com.toy.dworld.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 세션에서 이전 페이지를 가져옴
        String previousPage = (String) request.getSession().getAttribute("previousPage");

        if (previousPage != null) {
            // 이전 페이지로 리다이렉트
            redirectStrategy.sendRedirect(request, response, previousPage);
        } else {
            // 이전 페이지가 없으면 기본 페이지로 이동
            redirectStrategy.sendRedirect(request, response, "/articles");
        }
    }
}
