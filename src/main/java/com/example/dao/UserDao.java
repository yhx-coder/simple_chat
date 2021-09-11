package com.example.dao;

import com.example.pojo.User;
import org.apache.ibatis.annotations.Select;

/**
 * @author: ming
 * @date: 2021/9/11 13:07
 */
public interface UserDao {

    /**
     * 根据用户名和密码查询用户信息
     * @param username 用户名
     * @param password 密码
     * @return 登录状态
     */
    User queryByUsernameAndPassword(String username,String password);

    /**
     * 保存用户
     *
     * @return 失败为 -1 ，成功为 1
     */
    int saveUser(String username,String password);

}
