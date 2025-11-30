package com.gitrubbish.model;

import java.util.Date;

public class Comment {
    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private Date createTime;
    private String userName;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}