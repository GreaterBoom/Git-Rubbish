package com.gitrubbish.mapper;

import com.gitrubbish.model.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    
    @Select("<script>" +
            "SELECT * FROM article " +
            "WHERE 1=1 " +
            "<if test='userId != null'>AND user_id = #{userId}</if> " +
            "<if test='userId == null'>AND status = 1</if> " +
            "<if test='category != null and category != \"\"'>AND category = #{category}</if> " +
            "<if test='tags != null and tags != \"\"'>AND tags LIKE CONCAT('%', #{tags}, '%')</if> " +
            "<if test='keyword != null and keyword != \"\"'>AND (title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%'))</if> " +
            "ORDER BY create_time DESC " +
            "LIMIT #{offset}, #{size}" +
            "</script>")
    List<Article> findList(@Param("offset") int offset, 
                          @Param("size") int size,
                          @Param("category") String category,
                          @Param("tags") String tags,
                          @Param("keyword") String keyword,
                          @Param("userId") Long userId);
    
    @Select("<script>" +
            "SELECT COUNT(*) FROM article " +
            "WHERE 1=1 " +
            "<if test='userId != null'>AND user_id = #{userId}</if> " +
            "<if test='userId == null'>AND status = 1</if> " +
            "<if test='category != null and category != \"\"'>AND category = #{category}</if> " +
            "<if test='tags != null and tags != \"\"'>AND tags LIKE CONCAT('%', #{tags}, '%')</if> " +
            "<if test='keyword != null and keyword != \"\"'>AND (title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%'))</if>" +
            "</script>")
    int countList(@Param("category") String category,
                  @Param("tags") String tags,
                  @Param("keyword") String keyword,
                  @Param("userId") Long userId);
    
    @Select("SELECT * FROM article WHERE id = #{id}")
    Article findById(Long id);
    
    @Insert("INSERT INTO article(user_id, title, content, summary, category, tags, status, attachment_url, attachment_name) " +
            "VALUES(#{userId}, #{title}, #{content}, #{summary}, #{category}, #{tags}, #{status}, #{attachmentUrl}, #{attachmentName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Article article);
    
    @Update("UPDATE article SET title = #{title}, content = #{content}, summary = #{summary}, " +
            "category = #{category}, tags = #{tags}, status = #{status}, " +
            "attachment_url = #{attachmentUrl}, attachment_name = #{attachmentName} WHERE id = #{id}")
    void update(Article article);
    
    @Delete("DELETE FROM article WHERE id = #{id}")
    void delete(Long id);
    
    @Update("UPDATE article SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(Long id);
    
    @Update("UPDATE article SET like_count = like_count + 1 WHERE id = #{id}")
    void incrementLikeCount(Long id);
    
    @Update("UPDATE article SET comment_count = comment_count + 1 WHERE id = #{id}")
    void incrementCommentCount(Long id);
}