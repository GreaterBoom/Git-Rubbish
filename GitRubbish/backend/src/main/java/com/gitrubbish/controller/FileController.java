package com.gitrubbish.controller;

import com.gitrubbish.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "*")
public class FileController {
    
    // 文件上传目录 - 使用用户主目录下的绝对路径
    private static final String UPLOAD_DIR = System.getProperty("user.home") + File.separator + "gitrubbish-uploads" + File.separator + "attachments" + File.separator;
    
    // 图片上传目录
    private static final String IMAGE_UPLOAD_DIR = System.getProperty("user.home") + File.separator + "gitrubbish-uploads" + File.separator + "images" + File.separator;
    
    // 允许的文件类型
    private static final String[] ALLOWED_EXTENSIONS = {
        ".zip", ".rar", ".7z", ".tar", ".gz",
        ".java", ".py", ".js", ".html", ".css",
        ".txt", ".md", ".json", ".xml", ".yml"
    };
    
    // 允许的图片类型
    private static final String[] ALLOWED_IMAGE_EXTENSIONS = {
        ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"
    };
    
    // 最大文件大小 50MB
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024;
    
    // 最大图片大小 10MB
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024;
    
    // 静态初始化块 - 确保上传目录存在
    static {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            System.out.println("创建文件上传目录: " + UPLOAD_DIR);
        }
        
        File imageUploadDir = new File(IMAGE_UPLOAD_DIR);
        if (!imageUploadDir.exists()) {
            imageUploadDir.mkdirs();
            System.out.println("创建图片上传目录: " + IMAGE_UPLOAD_DIR);
        }
    }
    
    @PostMapping("/upload")
    public Result<?> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            // 验证登录
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return Result.error("请先登录");
            }
            
            // 验证文件
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            
            // 验证文件大小
            if (file.getSize() > MAX_FILE_SIZE) {
                return Result.error("文件大小不能超过50MB");
            }
            
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return Result.error("文件名无效");
            }
            
            // 验证文件扩展名
            String extension = getFileExtension(originalFilename);
            if (!isAllowedExtension(extension)) {
                return Result.error("不支持的文件类型");
            }
            
            // 生成唯一文件名
            String filename = UUID.randomUUID().toString() + extension;
            
            // 创建上传目录
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    return Result.error("无法创建上传目录");
                }
            }
            
            // 保存文件
            File destFile = new File(uploadDir, filename);
            file.transferTo(destFile);
            
            // 返回文件信息
            Map<String, Object> result = new HashMap<>();
            result.put("filename", filename);
            result.put("originalFilename", originalFilename);
            result.put("size", file.getSize());
            result.put("url", "/api/file/download?filename=" + filename);
            
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/download")
    public void downloadFile(@RequestParam String filename, 
                            @RequestParam(required = false) String originalName,
                            HttpServletResponse response) {
        FileInputStream fis = null;
        OutputStream os = null;
        
        try {
            File uploadDir = new File(UPLOAD_DIR);
            File file = new File(uploadDir, filename);
            
            System.out.println("下载请求 - 文件名: " + filename);
            System.out.println("上传目录: " + UPLOAD_DIR);
            System.out.println("完整路径: " + file.getAbsolutePath());
            System.out.println("文件存在: " + file.exists());
            System.out.println("文件大小: " + file.length() + " bytes");
            
            if (!file.exists() || !file.isFile()) {
                System.out.println("错误: 文件不存在或不是文件");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("text/plain; charset=UTF-8");
                response.getWriter().write("文件不存在: " + filename);
                return;
            }
            
            // 使用原始文件名，如果没有则使用UUID文件名
            String downloadFilename = originalName != null ? originalName : filename;
            
            // 处理文件名编码（支持中文）
            String encodedFilename = java.net.URLEncoder.encode(downloadFilename, "UTF-8").replaceAll("\\+", "%20");
            
            // 重置响应
            response.reset();
            
            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFilename + "\"; filename*=UTF-8''" + encodedFilename);
            response.setContentLengthLong(file.length());
            
            System.out.println("开始传输文件...");
            
            // 读取文件并写入响应
            fis = new FileInputStream(file);
            os = response.getOutputStream();
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalBytes = 0;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
            
            os.flush();
            System.out.println("文件传输完成，共传输: " + totalBytes + " bytes");
            
        } catch (Exception e) {
            System.err.println("下载失败: " + e.getMessage());
            e.printStackTrace();
            try {
                if (!response.isCommitted()) {
                    response.reset();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("text/plain; charset=UTF-8");
                    response.getWriter().write("下载失败: " + e.getMessage());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (fis != null) fis.close();
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @DeleteMapping("/delete")
    public Result<?> deleteFile(@RequestParam String filename, HttpServletRequest request) {
        try {
            // 验证登录
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return Result.error("请先登录");
            }
            
            File uploadDir = new File(UPLOAD_DIR);
            File file = new File(uploadDir, filename);
            if (file.exists() && file.delete()) {
                return Result.success("删除成功");
            }
            return Result.error("文件不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败");
        }
    }
    
    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot).toLowerCase() : "";
    }
    
    private boolean isAllowedExtension(String extension) {
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isAllowedImageExtension(String extension) {
        for (String allowed : ALLOWED_IMAGE_EXTENSIONS) {
            if (allowed.equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 图片上传接口 - 用于 Vditor 编辑器
     */
    @PostMapping("/upload-image")
    public Result<?> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            // 验证登录
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return Result.error("请先登录");
            }
            
            // 验证文件
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            
            // 验证文件大小
            if (file.getSize() > MAX_IMAGE_SIZE) {
                return Result.error("图片大小不能超过10MB");
            }
            
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return Result.error("文件名无效");
            }
            
            // 验证文件扩展名
            String extension = getFileExtension(originalFilename);
            if (!isAllowedImageExtension(extension)) {
                return Result.error("不支持的图片类型，仅支持 jpg, png, gif, bmp, webp");
            }
            
            // 生成唯一文件名
            String filename = UUID.randomUUID().toString() + extension;
            
            // 创建上传目录
            File imageUploadDir = new File(IMAGE_UPLOAD_DIR);
            if (!imageUploadDir.exists()) {
                boolean created = imageUploadDir.mkdirs();
                if (!created) {
                    return Result.error("无法创建上传目录");
                }
            }
            
            // 保存文件
            File destFile = new File(imageUploadDir, filename);
            file.transferTo(destFile);
            
            // 返回文件信息
            Map<String, Object> result = new HashMap<>();
            result.put("filename", filename);
            result.put("originalFilename", originalFilename);
            result.put("size", file.getSize());
            result.put("url", "/file/image/" + filename);
            
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("图片上传失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取图片 - 用于访问上传的图片
     */
    @GetMapping("/image/{filename}")
    public void getImage(@PathVariable String filename, HttpServletResponse response) {
        FileInputStream fis = null;
        OutputStream os = null;
        
        try {
            File imageDir = new File(IMAGE_UPLOAD_DIR);
            File file = new File(imageDir, filename);
            
            if (!file.exists() || !file.isFile()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            // 根据文件扩展名设置 Content-Type
            String extension = getFileExtension(filename);
            String contentType = "image/jpeg";
            switch (extension) {
                case ".png":
                    contentType = "image/png";
                    break;
                case ".gif":
                    contentType = "image/gif";
                    break;
                case ".bmp":
                    contentType = "image/bmp";
                    break;
                case ".webp":
                    contentType = "image/webp";
                    break;
            }
            
            response.setContentType(contentType);
            response.setContentLengthLong(file.length());
            
            // 读取文件并写入响应
            fis = new FileInputStream(file);
            os = response.getOutputStream();
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            
            os.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
