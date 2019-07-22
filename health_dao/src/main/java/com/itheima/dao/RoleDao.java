package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

public interface RoleDao {
    /**
     * 通过用户的ID获取用户所拥有的角色集合
     * @param userId
     * @return
     */
    Set<Role> findByUserId(Integer userId);
}
