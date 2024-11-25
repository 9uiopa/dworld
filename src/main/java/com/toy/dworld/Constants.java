package com.toy.dworld;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final int PAGE_SIZE = 10;  // 페이징 시, 한 페이지 내 게시글 양
    public static final int HOT_ARTICLE_THRESHOLD = 2; // 인기 게시글 추천 커트라인
    public static final List<String> EXCLUDED_PATHS = Arrays.asList( // 최근 접속 페이지에 포함하지 않을 url
            "/login",
            "/static/",
            "/images/",
            "/css/",
            "/js/",
            "/api/"
    );
}
