package com.exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication()
@EnableCaching(proxyTargetClass=true) // 支持开启事务
public class ExamsystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamsystemApplication.class, args);
    }
}

