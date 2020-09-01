package com.ustc.software.dao;

import com.ustc.software.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1320:56
 */
@Mapper
@Repository
public interface UserMapper {
    //根据name id  email查User
    User findUserByName(String name);
    User findUserById(int id);
    User findUserByEmail(String email);
    //添加用户
    int insertUser(User user);
    //修改用户  id 改状态
    int updateUserStatus(int id,int status);
    //根据id改头像
    int updateUserHeader(int id,String headerURL);
    //根据id改密码
    int updateUserPassword(int id,String password);
}
