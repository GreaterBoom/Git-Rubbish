package com.gitrubbish.mapper;

import com.gitrubbish.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);
    
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);
    
    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email);
    
    @Insert("INSERT INTO user(email, username, password, nickname, avatar, bio) " +
            "VALUES(#{email}, #{username}, #{password}, #{nickname}, #{avatar}, #{bio})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);
    
    @Update("UPDATE user SET nickname = #{nickname}, bio = #{bio}, avatar = #{avatar} WHERE id = #{id}")
    void update(User user);
    
    @Select("SELECT id, username, email, nickname, avatar, bio, create_time FROM user ORDER BY create_time DESC")
    List<User> findAll();
}