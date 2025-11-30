-- 创建简历表
USE gitrubbish;

CREATE TABLE IF NOT EXISTS `resume` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  `title` VARCHAR(100) DEFAULT NULL COMMENT '职位/标题',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话',
  `location` VARCHAR(100) DEFAULT NULL COMMENT '所在地',
  `website` VARCHAR(255) DEFAULT NULL COMMENT '个人网站',
  `github` VARCHAR(255) DEFAULT NULL COMMENT 'GitHub',
  `summary` TEXT DEFAULT NULL COMMENT '个人简介',
  `skills` TEXT DEFAULT NULL COMMENT '技能（JSON格式）',
  `experience` TEXT DEFAULT NULL COMMENT '工作经历（JSON格式）',
  `education` TEXT DEFAULT NULL COMMENT '教育经历（JSON格式）',
  `projects` TEXT DEFAULT NULL COMMENT '项目经历（JSON格式）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户简历表';
