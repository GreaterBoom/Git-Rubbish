package com.gitrubbish.controller;

import com.gitrubbish.common.Result;
import com.gitrubbish.mapper.ChatMapper;
import com.gitrubbish.mapper.UserMapper;
import com.gitrubbish.model.ChatMessage;
import com.gitrubbish.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {
    
    @Autowired
    private ChatMapper chatMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @GetMapping("/messages")
    public Result<?> getMessages(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "50") Integer size,
            @RequestParam(required = false) Long afterId) {
        try {
            int offset = (page - 1) * size;
            
            List<ChatMessage> messages;
            int total;
            
            if (afterId != null) {
                // 获取指定ID之后的新消息
                messages = chatMapper.findAfter(afterId, size);
                total = messages.size();
            } else {
                // 获取最新消息
                messages = chatMapper.findLatest(offset, size);
                total = chatMapper.count();
            }
            
            // 填充用户信息
            for (ChatMessage message : messages) {
                User user = userMapper.findById(message.getUserId());
                if (user != null) {
                    message.setUserName(user.getNickname() != null ? user.getNickname() : user.getUsername());
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", messages);
            result.put("total", total);
            
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取消息失败");
        }
    }
    
    @PostMapping("/send")
    public Result<?> sendMessage(@RequestBody ChatMessage message, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            message.setUserId(userId);
            
            chatMapper.insert(message);
            
            // 填充用户信息
            User user = userMapper.findById(userId);
            if (user != null) {
                message.setUserName(user.getNickname() != null ? user.getNickname() : user.getUsername());
            }
            
            return Result.success(message);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("发送消息失败");
        }
    }
}