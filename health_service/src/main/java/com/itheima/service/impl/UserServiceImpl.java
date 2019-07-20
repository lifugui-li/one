package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findByUsername(String username) {
        // 1. 通过用户名获取用户信息
        /*User user = userDao.findByUsername(username);
        if(null != user) {
            // 2. 通过用户的ID获取用户所拥有的角色集合,设置给用户
            Set<Role> roles = roleDao.findByUserId(user.getId());
            if(null != roles && roles.size() > 0) {
                user.setRoles(roles);
                // 3. 遍历角色集合，通过角色编号获取权限集合, 设置给角色
                for (Role role : roles) {
                    // 通过角色编号获取这个角色所拥有的权限集合
                    Set<Permission> permissions = permissionDao.findByRoleId(role.getId());
                    role.setPermissions(permissions);
                }
            }
            return user;
        }
        return null;*/
        return userDao.findUserRolePermissionByUsername(username);
    }
}
