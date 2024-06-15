package com.toy.dworld;

import com.toy.dworld.repo.ArticleIndexRepository;
//import com.toy.dworld.repo.jpa.ArticleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableElasticsearchRepositories(basePackageClasses = ArticleIndexRepository.class)
//@EnableJpaRepositories(basePackageClasses = ArticleRepository.class,
//        excludeFilters = @ComponentScan.Filter( // 제외 필터
//        type = FilterType.ASSIGNABLE_TYPE, // 해당 클래스를 확장하거나 구현한 모든 클래스를 대상으로 필터링
//        classes = ArticleIndexRepository.class)) // 스캔 대상에서 EsArticleRepository 제외
public class DworldApplication {

    public static void main(String[] args) {
        SpringApplication.run(DworldApplication.class, args);
    }

}
