package com.toy.dworld.config.security;

import com.toy.dworld.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import static com.toy.dworld.Constants.EXCLUDED_PATHS;

@Component
public class PreviousPageInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 전체 URL 가져오기
        String requestUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (queryString != null) {
            requestUrl += "?" + queryString;
        }

        // 현재 요청한 페이지를 세션에 저장
        if (!isExcludedPath(request.getRequestURI())) {
            request.getSession().setAttribute("previousPage", requestUrl);
        }

        return true;
    }

    private boolean isExcludedPath(String uri) {
        return EXCLUDED_PATHS.stream().anyMatch(uri::contains);
    }
}
