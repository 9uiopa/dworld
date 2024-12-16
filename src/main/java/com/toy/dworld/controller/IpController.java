package com.toy.dworld.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 로드밸런싱 적용 후 어느 인스턴스인지 확인하기 위한 헤더 읽기 작업용
@Slf4j
@RequiredArgsConstructor
@RestController
public class IpController {
    @GetMapping("/client-ip")
    public String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null) {
            log.debug("### xForwardedFor : " + xForwardedFor);
            // X-Forwarded-For에 클라이언트 IP가 포함
            return "Client IP: " + xForwardedFor;
        }
        // 기본적인 RemoteAddr 활용 (프록시 뒤가 아닌 경우)
        return "X-Forwarded-For 헤더 없음. Client IP: " + request.getRemoteAddr();
    }
}
