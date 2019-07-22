package com.itheima.dao;

import com.itheima.pojo.User;

public interface UserDao {
    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 获取用户的详细权限信息
     * @param username
     * @return
     */
    User findUserRolePermissionByUsername(String username);
}
