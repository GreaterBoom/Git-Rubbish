-- GitRubbish Database Initialization Script

CREATE DATABASE IF NOT EXISTS gitrubbish DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gitrubbish;

-- Drop existing tables to ensure clean installation
DROP TABLE IF EXISTS `chat_message`;
DROP TABLE IF EXISTS `collect`;
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `article`;
DROP TABLE IF EXISTS `user`;

-- User table
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(100) DEFAULT NULL,
  `nickname` VARCHAR(50) DEFAULT NULL,
  `avatar` VARCHAR(255) DEFAULT NULL,
  `bio` VARCHAR(200) DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Article table
CREATE TABLE `article` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(200) NOT NULL,
  `content` LONGTEXT NOT NULL,
  `summary` VARCHAR(500) DEFAULT NULL,
  `category` VARCHAR(50) DEFAULT NULL,
  `tags` VARCHAR(200) DEFAULT NULL,
  `status` TINYINT DEFAULT 0,
  `like_count` INT DEFAULT 0,
  `view_count` INT DEFAULT 0,
  `comment_count` INT DEFAULT 0,
  `attachment_url` VARCHAR(500) DEFAULT NULL,
  `attachment_name` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Comment table
CREATE TABLE `comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `article_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `content` TEXT NOT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Collect table
CREATE TABLE `collect` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `article_id` BIGINT NOT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_article` (`user_id`, `article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Chat message table
CREATE TABLE `chat_message` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `content` TEXT NOT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert test data (English only)
INSERT INTO `user` (`username`, `password`, `nickname`, `email`) VALUES
('admin', MD5('123456'), 'Admin', 'admin@gitrubbish.com');

INSERT INTO `article` (`user_id`, `title`, `content`, `summary`, `category`, `tags`, `status`, `like_count`, `view_count`) VALUES
(1, 'GitRubbish Quick Start Guide', '# GitRubbish Quick Start\n\nWelcome to GitRubbish Blog Platform!\n\n## Features\n\n- Minimalist black and white design\n- Markdown editor support\n- Code highlighting\n- Real-time chat\n\n## Getting Started\n\n1. Register account\n2. Login system\n3. Start writing\n\nEnjoy blogging!', 'Welcome to GitRubbish Blog Platform', 'Tools', 'GitRubbish,Blog,Markdown', 1, 10, 100),
(1, 'Vue3 Development Guide', '# Vue3 Development\n\n## Create Project\n\n```bash\nnpm create vite@latest my-app\ncd my-app\nnpm install\nnpm run dev\n```\n\n## Composition API\n\n```javascript\nimport { ref, computed } from "vue"\n\nconst count = ref(0)\nconst double = computed(() => count.value * 2)\n```\n\nStart your Vue3 journey!', 'Vue3 development with Vite', 'Frontend', 'Vue,Vite,JavaScript', 1, 25, 230),
(1, 'Spring Boot Tutorial', '# Spring Boot Development\n\n## Core Annotations\n\n- @SpringBootApplication\n- @RestController\n- @Service\n- @Repository\n\n## Example Controller\n\n```java\n@RestController\npublic class HelloController {\n    @GetMapping("/hello")\n    public String hello() {\n        return "Hello World!";\n    }\n}\n```\n\nBuild efficient microservices!', 'Spring Boot microservice development', 'Backend', 'Spring Boot,Java,Backend', 1, 42, 356);