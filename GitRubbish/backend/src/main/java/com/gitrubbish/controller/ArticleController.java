package com.gitrubbish.controller;

import com.gitrubbish.common.Result;
import com.gitrubbish.mapper.ArticleMapper;
import com.gitrubbish.mapper.UserMapper;
import com.gitrubbish.model.Article;
import com.gitrubbish.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
@CrossOrigin(origins = "*")
public class ArticleController {
    
    @Autowired
    private ArticleMapper articleMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @GetMapping("/list")
    public Result<?> getArticleList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean myArticles,
            HttpServletRequest request) {
        try {
            int offset = (page - 1) * size;
            
            Long userId = null;
            if (myArticles != null && myArticles) {
                userId = (Long) request.getAttribute("userId");
            }
            
            List<Article> articles = articleMapper.findList(offset, size, category, tags, keyword, userId);
            int total = articleMapper.countList(category, tags, keyword, userId);
            
            // 填充作者信息
            for (Article article : articles) {
                User user = userMapper.findById(article.getUserId());
                if (user != null) {
                    article.setAuthorName(user.getNickname() != null ? user.getNickname() : user.getUsername());
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", articles);
            result.put("total", total);
            result.put("pages", (int) Math.ceil((double) total / size));
            result.put("current", page);
            
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取文章列表失败");
        }
    }
    
    @GetMapping("/detail")
    public Result<?> getArticleDetail(@RequestParam Long id) {
        try {
            Article article = articleMapper.findById(id);
            if (article == null) {
                return Result.error("文章不存在");
            }
            
            // 增加浏览量
            articleMapper.incrementViewCount(id);
            article.setViewCount(article.getViewCount() + 1);
            
            // 填充作者信息
            User user = userMapper.findById(article.getUserId());
            if (user != null) {
                article.setAuthorName(user.getNickname() != null ? user.getNickname() : user.getUsername());
            }
            
            return Result.success(article);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取文章详情失败");
        }
    }
    
    @PostMapping
    public Result<?> saveArticle(@RequestBody Article article, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            
            if (article.getId() == null) {
                // 新建文章
                article.setUserId(userId);
                articleMapper.insert(article);
            } else {
                // 更新文章
                Article existing = articleMapper.findById(article.getId());
                if (existing == null) {
                    return Result.error("文章不存在");
                }
                if (!existing.getUserId().equals(userId)) {
                    return Result.error("无权限修改");
                }
                articleMapper.update(article);
            }
            
            return Result.success(article);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("保存文章失败");
        }
    }
    
    @DeleteMapping
    public Result<?> deleteArticle(@RequestParam Long id, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            
            Article article = articleMapper.findById(id);
            if (article == null) {
                return Result.error("文章不存在");
            }
            if (!article.getUserId().equals(userId)) {
                return Result.error("无权限删除");
            }
            
            articleMapper.delete(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败");
        }
    }
    
    @PostMapping("/like")
    public Result<?> likeArticle(@RequestBody Map<String, Long> params) {
        try {
            Long articleId = params.get("articleId");
            articleMapper.incrementLikeCount(articleId);
            return Result.success("点赞成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("点赞失败");
        }
    }
    
    @PostMapping("/collect")
    public Result<?> collectArticle(@RequestBody Map<String, Long> params) {
        try {
            return Result.success("收藏成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("收藏失败");
        }
    }
}