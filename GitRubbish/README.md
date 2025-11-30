# GitRubbish 博客平台

一个轻量级、现代化的博客社区平台，采用前后端分离架构，提供文章发布、用户交互、简历管理等功能。

## 项目简介

GitRubbish 是一个功能完善的博客平台，支持 Markdown 编辑、实时聊天、用户管理、简历展示等功能。界面采用 Neobrutalism 设计风格，简洁而富有个性。

## 技术栈

### 后端

- Java 8+
- Spring Boot 2.x
- MyBatis
- MySQL 8.0
- JWT 认证

### 前端

- HTML5 / CSS3 / JavaScript
- Tailwind CSS
- Vditor (Markdown 编辑器)
- Axios (HTTP 客户端)

## 核心功能

### 用户系统

- 用户注册与登录
- JWT Token 认证
- 用户资料管理

### 文章管理

- Markdown 文章编写
- Vditor 富文本编辑器（功能不完全！！！）
- 文章发布与编辑
- 文章浏览与搜索
- 文章评论功能
- 文章点赞功能

### 社交功能

- 实时聊天系统
- 用户关注功能
- 社区成员列表
- 用户主页展示

### 简历系统

- 在线简历编辑
- 简历预览与导出
- 个人信息展示

### 其他功能

未实现：）

## 项目结构

```
GitRubbish/
├── backend/                 # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/gitrubbish/
│   │       │       ├── controller/    # 控制器
│   │       │       ├── service/       # 服务层
│   │       │       ├── mapper/        # 数据访问层
│   │       │       ├── model/         # 实体类
│   │       │       ├── config/        # 配置类
│   │       │       └── util/          # 工具类
│   │       └── resources/
│   │           ├── application.yml    # 配置文件
│   │           └── mapper/            # MyBatis XML
│   └── pom.xml
│
├── frontend/                # 前端项目
│   ├── js/
│   │   └── common.js        # 公共 JS
│   ├── picture/             # 图片资源
│   ├── index.html           # 首页
│   ├── login.html           # 登录页
│   ├── register.html        # 注册页
│   ├── article-edit-vditor.html  # 文章编辑
│   ├── article-detail.html  # 文章详情
│   ├── chat.html            # 聊天页面
│   ├── profile.html         # 个人资料
│   ├── resume-edit.html     # 简历编辑
│   ├── resume-view.html     # 简历查看
│   └── users.html           # 用户列表
│
├── database/                # 数据库脚本
│   ├── init.sql             # 完整初始化脚本
│   ├── init-clean.sql       # 清空数据库
│   └── init-simple.sql      # 简化版初始化
│
├── uploads/                 # 文件上传目录
└── README.md
```

## 快速开始

### 环境要求

- JDK 8 或更高版本
- MySQL 8.0
- Maven 3.6+
- 现代浏览器（Chrome、Firefox、Edge 等）

### 数据库配置

1. 创建数据库：
   
   ```sql
   CREATE DATABASE gitrubbish CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. 导入数据库脚本：
   
   ```bash
   mysql -u root -p gitrubbish < database/init.sql
   ```

### 后端启动

1. 修改配置文件 `backend/src/main/resources/application.yml`：
   
   ```yaml
   spring:
   datasource:
    url: jdbc:mysql://localhost:3306/gitrubbish
    username: your_username
    password: your_password
   ```

2. 编译并启动：
   
   ```bash
   cd backend
   mvn clean package
   java -jar target/gitrubbish-backend.jar
   ```

或使用 IDE 直接运行主类。

### 前端启动

1. 修改 API 地址（如需要）：
   编辑 `frontend/js/common.js`，修改 `API_BASE_URL`。

2. 使用 Web 服务器运行前端：
   
   ```bash
   # 使用 Python
   cd frontend
   python -m http.server 8080
   
   ```

# 或使用 Node.js http-server

npx http-server frontend -p 8080

```

3. 访问：http://localhost:8080

## 默认账号

系统初始化后会创建默认管理员账号：
- 用户名：admin
- 密码：admin123

建议首次登录后立即修改密码。

## API 接口

### 用户相关
- POST `/user/register` - 用户注册
- POST `/user/login` - 用户登录
- GET `/user/info` - 获取用户信息
- PUT `/user/update` - 更新用户信息
- POST `/user/upload-avatar` - 上传头像

### 文章相关
- GET `/article/list` - 文章列表
- GET `/article/{id}` - 文章详情
- POST `/article/create` - 创建文章
- PUT `/article/update` - 更新文章
- DELETE `/article/{id}` - 删除文章
- POST `/article/{id}/like` - 点赞文章

### 评论相关
- GET `/comment/list/{articleId}` - 评论列表
- POST `/comment/create` - 发表评论
- DELETE `/comment/{id}` - 删除评论

### 聊天相关
- GET `/chat/messages` - 获取聊天消息
- POST `/chat/send` - 发送消息

### 简历相关
- GET `/resume/{userId}` - 获取简历
- POST `/resume/save` - 保存简历
- PUT `/resume/update` - 更新简历

### 文件相关
- POST `/file/upload` - 文件上传
- GET `/file/download/{filename}` - 文件下载

## 配置说明

### JWT 配置
在 `application.yml` 中配置 JWT 密钥和过期时间：
```yaml
jwt:
  secret: your-secret-key
  expiration: 86400000  # 24小时
```

### 文件上传配置

```yaml
file:
  upload-dir: ./uploads
  max-size: 10485760  # 10MB
```

### 跨域配置

已在 `WebConfig.java` 中配置 CORS，允许前端跨域访问。

## 部署说明

### 后端部署

1. 打包项目：
   
   ```bash
   mvn clean package -DskipTests
   ```

2. 上传 JAR 文件到服务器

3. 启动服务：
   
   ```bash
   nohup java -jar gitrubbish-backend.jar > app.log 2>&1 &
   ```

### 前端部署

1. 将 `frontend` 目录下所有文件上传到 Web 服务器

2. 配置 Nginx（推荐）：
   
   ```nginx
   server {
    listen 80;
    server_name your-domain.com;
   
    root /path/to/frontend;
    index index.html;
   
    location / {
        try_files $uri $uri/ /index.html;
    }
   
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
   }
   ```

## 开发指南

### 添加新功能

1. 后端：
   
   - 在 `model` 包中创建实体类
   - 在 `mapper` 包中创建 Mapper 接口和 XML
   - 在 `service` 包中实现业务逻辑
   - 在 `controller` 包中创建 REST API

2. 前端：
   
   - 创建对应的 HTML 页面
   - 使用 Axios 调用后端 API
   - 使用 Tailwind CSS 进行样式设计

### 代码规范

- 后端遵循阿里巴巴 Java 开发规范
- 前端使用 ES6+ 语法
- 统一使用 UTF-8 编码
- 注释使用中文

## 常见问题

### 1. 数据库连接失败

检查 MySQL 服务是否启动，用户名密码是否正确。

### 2. 跨域问题

确保后端 CORS 配置正确，前端 API 地址配置正确。

### 3. 文件上传失败

检查 `uploads` 目录是否存在且有写入权限。

### 4. JWT Token 过期

重新登录获取新的 Token。

## 更新日志

### v1.0.0 (2024-11)

- 完成基础用户系统
- 实现文章发布与管理
- 添加评论和点赞功能
- 集成 Vditor 编辑器
- 实现实时聊天功能
- 添加简历管理系统
- 优化登录注册页面 UI
- 添加动态背景特效

## 许可证

（狗头）

## 联系方式

- 官方QQ群：849498477

## 致谢

感谢以下开源项目：

- Spring Boot
- Vditor
- Tailwind CSS
- Axios
