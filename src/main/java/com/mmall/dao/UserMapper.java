package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    User selectLogin(@Param("username") String username,@Param("password") String password);

    int checkEmail(String email);

    String selectQuestionByUsername(String username);

    @Select("select count(1) from mmall_user where username=#{username} and question=#{question} and answer=#{answer}")
    int checkAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    @Update("update mmall_user set password=#{passwordNew},update_time=now() where username = #{username}")
    int updatePasswordByUsername(@Param("username") String username,@Param("passwordNew") String passwordNew);

    @Select("select count(1) from mmall_user where id=#{userId}")
    int checkPassword(@Param("password") String password,@Param("userId") Integer userId);

    @Select("select count(1) from mmall_user where email=#{email} and id!=#{userId}")
    int checkEmailByUserId(@Param("email") String email,@Param("userId") Integer userId);
}