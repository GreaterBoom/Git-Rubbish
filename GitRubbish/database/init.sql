-- GitRubbish 数据库初始化脚本

CREATE DATABASE IF NOT EXISTS gitrubbish DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gitrubbish;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email` VARCHAR(100) NOT NULL COMMENT '邮箱',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（MD5加密）',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
  `bio` VARCHAR(200) DEFAULT NULL COMMENT '个人简介',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 文章表
CREATE TABLE IF NOT EXISTS `article` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `title` VARCHAR(200) NOT NULL COMMENT '文章标题',
  `content` LONGTEXT NOT NULL COMMENT '文章内容（Markdown）',
  `summary` VARCHAR(500) DEFAULT NULL COMMENT '文章摘要',
  `category` VARCHAR(50) DEFAULT NULL COMMENT '分类',
  `tags` VARCHAR(200) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0=草稿，1=已发布',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `view_count` INT DEFAULT 0 COMMENT '浏览数',
  `comment_count` INT DEFAULT 0 COMMENT '评论数',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `article_id` BIGINT NOT NULL COMMENT '文章ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 收藏表
CREATE TABLE IF NOT EXISTS `collect` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `article_id` BIGINT NOT NULL COMMENT '文章ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_article` (`user_id`, `article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 聊天消息表
CREATE TABLE IF NOT EXISTS `chat_message` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `content` TEXT NOT NULL COMMENT '消息内容',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';

-- 插入测试数据
INSERT INTO `user` (`email`, `username`, `password`, `nickname`) VALUES
('admin@gitrubbish.com', 'admin', MD5('123456'), 'Admin');

INSERT INTO `article` (`user_id`, `title`, `content`, `summary`, `category`, `tags`, `status`, `like_count`, `view_count`) VALUES
(1, 'GitRubbish 使用指南', '# GitRubbish 使用指南\n\n欢迎使用 GitRubbish 博客平台！\n\n## 功能特色\n\n- 极简黑白风格设计\n- Markdown 编辑支持\n- 代码高亮显示\n\n## 快速开始\n\n1. 注册账号\n2. 登录系统\n3. 开始写作', '欢迎使用 GitRubbish 博客平台', '工具推荐', 'GitRubbish,博客', 1, 10, 100),
(1, 'Vue3 快速上手', '# Vue3 快速上手\n\n## 创建项目\n\n```bash\nnpm create vite@latest\n```\n\n开始你的Vue3之旅！', '使用Vite快速搭建Vue3项目', '技术笔记', 'Vue,Vite,前端', 1, 25, 230),
(1, 'Spring Boot 实战', '# Spring Boot 开发\n\n## 核心注解\n\n- @SpringBootApplication\n- @RestController\n- @Service', 'Spring Boot微服务实践', '项目经验', 'Spring Boot,Java', 1, 42, 356);