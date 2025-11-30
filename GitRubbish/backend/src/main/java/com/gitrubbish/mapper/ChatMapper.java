package com.gitrubbish.mapper;

import com.gitrubbish.model.ChatMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatMapper {
    
    @Select("SELECT * FROM chat_message ORDER BY create_time DESC LIMIT #{offset}, #{size}")
    List<ChatMessage> findLatest(@Param("offset") int offset, @Param("size") int size);
    
    @Select("SELECT * FROM chat_message WHERE id > #{afterId} ORDER BY create_time ASC LIMIT #{size}")
    List<ChatMessage> findAfter(@Param("afterId") Long afterId, @Param("size") int size);
    
    @Select("SELECT COUNT(*) FROM chat_message")
    int count();
    
    @Insert("INSERT INTO chat_message(user_id, content) VALUES(#{userId}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ChatMessage message);
}