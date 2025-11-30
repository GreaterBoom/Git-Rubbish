package com.gitrubbish.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.gitrubbish.common.Result;
import com.gitrubbish.mapper.UserMapper;
import com.gitrubbish.model.User;
import com.gitrubbish.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        try {
            // 检查用户名是否已存在
            User existUsername = userMapper.findByUsername(user.getUsername());
            if (existUsername != null) {
                return Result.error("用户名已被使用");
            }
            
            // 加密密码
            user.setPassword(DigestUtil.md5Hex(user.getPassword()));
            
            // 设置默认昵称
            if (user.getNickname() == null || user.getNickname().isEmpty()) {
                user.setNickname(user.getUsername());
            }
            
            // 邮箱可选，如果没有提供则设置为空
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                user.setEmail(user.getUsername() + "@gitrubbish.local");
            }
            
            userMapper.insert(user);
            return Result.success("注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("注册失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params) {
        try {
            String username = params.get("username");
            String password = params.get("password");
            
            // 查询用户（只使用用户名登录）
            User user = userMapper.findByUsername(username);
            
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 验证密码
            String encryptedPassword = DigestUtil.md5Hex(password);
            if (!encryptedPassword.equals(user.getPassword())) {
                return Result.error("密码错误");
            }
            
            // 生成JWT令牌
            String token = jwtUtil.generateToken(user.getId());
            
            // 返回用户信息和令牌
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("email", user.getEmail());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("avatar", user.getAvatar());
            userInfo.put("bio", user.getBio());
            
            result.put("user", userInfo);
            
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("登录失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/profile")
    public Result<?> getProfile(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            User user = userMapper.findById(userId);
            
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("email", user.getEmail());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("avatar", user.getAvatar());
            userInfo.put("bio", user.getBio());
            
            return Result.success(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取用户信息失败");
        }
    }
    
    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestBody User user, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            user.setId(userId);
            userMapper.update(user);
            return Result.success("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新失败");
        }
    }
    
    @GetMapping("/list")
    public Result<?> getUserList() {
        try {
            // 获取所有用户（不包含密码）
            List<User> users = userMapper.findAll();
            
            // 移除密码字段
            for (User user : users) {
                user.setPassword(null);
            }
            
            return Result.success(users);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取用户列表失败");
        }
    }
}