package com.gitrubbish.controller;

import com.gitrubbish.common.Result;
import com.gitrubbish.mapper.ResumeMapper;
import com.gitrubbish.model.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "*")
public class ResumeController {
    
    @Autowired
    private ResumeMapper resumeMapper;
    
    // 获取简历（通过用户ID）
    @GetMapping
    public Result<?> getResume(@RequestParam Long userId) {
        try {
            Resume resume = resumeMapper.findByUserId(userId);
            if (resume == null) {
                return Result.error("简历不存在");
            }
            return Result.success(resume);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取简历失败");
        }
    }
    
    // 获取当前用户的简历
    @GetMapping("/my")
    public Result<?> getMyResume(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            Resume resume = resumeMapper.findByUserId(userId);
            return Result.success(resume);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取简历失败");
        }
    }
    
    // 保存或更新简历
    @PostMapping
    public Result<?> saveResume(@RequestBody Resume resume, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            resume.setUserId(userId);
            
            Resume existing = resumeMapper.findByUserId(userId);
            if (existing == null) {
                // 新建简历
                resumeMapper.insert(resume);
            } else {
                // 更新简历
                resumeMapper.update(resume);
            }
            
            return Result.success(resume);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("保存简历失败");
        }
    }
    
    // 删除简历
    @DeleteMapping
    public Result<?> deleteResume(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            resumeMapper.deleteByUserId(userId);
            return Result.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败");
        }
    }
}
