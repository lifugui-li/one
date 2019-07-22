package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    /**
     * 通过角色编号获取这个角色所拥有的权限集合
     * @param roleId
     * @return
     */
    Set<Permission> findByRoleId(Integer roleId);
}
