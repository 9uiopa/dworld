package com.toy.dworld;

import com.toy.dworld.repo.ArticleIndexRepository;
//import com.toy.dworld.repo.ArticleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class DworldApplication {

    public static void main(String[] args) {
        SpringApplication.run(DworldApplication.class, args);
    }

}
