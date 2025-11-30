package com.gitrubbish.mapper;

import com.gitrubbish.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    
    @Select("SELECT * FROM comment WHERE article_id = #{articleId} ORDER BY create_time DESC LIMIT #{offset}, #{size}")
    List<Comment> findByArticleId(@Param("articleId") Long articleId, 
                                   @Param("offset") int offset, 
                                   @Param("size") int size);
    
    @Select("SELECT COUNT(*) FROM comment WHERE article_id = #{articleId}")
    int countByArticleId(Long articleId);
    
    @Insert("INSERT INTO comment(article_id, user_id, content) VALUES(#{articleId}, #{userId}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Comment comment);
}