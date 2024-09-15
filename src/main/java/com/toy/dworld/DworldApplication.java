package com.toy.dworld;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class DworldApplication {
    public static void main(String[] args) {
        SpringApplication.run(DworldApplication.class, args);
    }

}
