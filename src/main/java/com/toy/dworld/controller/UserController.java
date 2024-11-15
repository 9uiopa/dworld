package com.toy.dworld.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class UserController {
    @GetMapping("/user-info")
    public String getUserInfo() {
        // 인증 유저 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof OAuth2User) {
                String name = ((OAuth2User) principal).getName();
            } else {
                log.info("@@@@@@@@@@@@@ 현재 로그인된 사용자: " + principal.toString());
            }
        } else {
            log.info("로그인된 사용자가 없습니다.");
        }
        return "login"; // 해당 페이지로 리디렉션 또는 다른 처리
    }
}
