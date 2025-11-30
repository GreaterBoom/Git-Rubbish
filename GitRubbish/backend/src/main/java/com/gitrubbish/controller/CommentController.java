package com.gitrubbish.controller;

import com.gitrubbish.common.Result;
import com.gitrubbish.mapper.ArticleMapper;
import com.gitrubbish.mapper.CommentMapper;
import com.gitrubbish.mapper.UserMapper;
import com.gitrubbish.model.Comment;
import com.gitrubbish.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin(origins = "*")
public class CommentController {
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ArticleMapper articleMapper;
    
    @GetMapping("/list")
    public Result<?> getCommentList(
            @RequestParam Long articleId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            int offset = (page - 1) * size;
            
            List<Comment> comments = commentMapper.findByArticleId(articleId, offset, size);
            int total = commentMapper.countByArticleId(articleId);
            
            // 填充用户信息
            for (Comment comment : comments) {
                User user = userMapper.findById(comment.getUserId());
                if (user != null) {
                    comment.setUserName(user.getNickname() != null ? user.getNickname() : user.getUsername());
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", comments);
            result.put("total", total);
            
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取评论失败");
        }
    }
    
    @PostMapping
    public Result<?> addComment(@RequestBody Comment comment, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            comment.setUserId(userId);
            
            commentMapper.insert(comment);
            
            // 更新文章评论数
            articleMapper.incrementCommentCount(comment.getArticleId());
            
            return Result.success("评论成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("评论失败");
        }
    }
}