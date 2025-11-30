package com.gitrubbish;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gitrubbish.mapper")
public class GitRubbishApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitRubbishApplication.class, args);
        System.out.println("========================================");
        System.out.println("GitRubbish 博客平台启动成功！");
        System.out.println("后端API地址: http://localhost:8081/api");
        System.out.println("========================================");
    }
}