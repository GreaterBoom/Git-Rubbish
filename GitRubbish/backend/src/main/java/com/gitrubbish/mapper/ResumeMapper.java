package com.gitrubbish.mapper;

import com.gitrubbish.model.Resume;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ResumeMapper {
    
    @Select("SELECT * FROM resume WHERE user_id = #{userId}")
    Resume findByUserId(Long userId);
    
    @Select("SELECT * FROM resume WHERE id = #{id}")
    Resume findById(Long id);
    
    @Insert("INSERT INTO resume(user_id, name, title, avatar, email, phone, location, website, github, summary, skills, experience, education, projects) " +
            "VALUES(#{userId}, #{name}, #{title}, #{avatar}, #{email}, #{phone}, #{location}, #{website}, #{github}, #{summary}, #{skills}, #{experience}, #{education}, #{projects})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Resume resume);
    
    @Update("UPDATE resume SET name = #{name}, title = #{title}, avatar = #{avatar}, email = #{email}, " +
            "phone = #{phone}, location = #{location}, website = #{website}, github = #{github}, " +
            "summary = #{summary}, skills = #{skills}, experience = #{experience}, " +
            "education = #{education}, projects = #{projects} WHERE user_id = #{userId}")
    void update(Resume resume);
    
    @Delete("DELETE FROM resume WHERE user_id = #{userId}")
    void deleteByUserId(Long userId);
}
