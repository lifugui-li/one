package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("securityUserService")
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;
    
    @Reference 

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名从数据库查询用户信息, 用户下有角色，角色下有权限
        com.itheima.pojo.User loginUser = userService.findByUsername(username);
        if(null != loginUser){
            // 能找用户信息
            // 授权, 用户的权限集合
            List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
            // 一个用户可以拥有多个角色
            Set<Role> roles = loginUser.getRoles();
            if(null != roles && roles.size() > 0){
                SimpleGrantedAuthority sga = null;
                for (Role role : roles) {
                    // 添加角色的权限
                    sga = new SimpleGrantedAuthority(role.getKeyword());
                    authorityList.add(sga);
                    // 角色有拥有多个权限
                    Set<Permission> permissions = role.getPermissions();
                    if(null != permissions &&  permissions.size() > 0){
                        for (Permission permission : permissions) {
                            // 处理权限
                            sga = new SimpleGrantedAuthority(permission.getKeyword());
                            authorityList.add(sga);
                        }
                    }
                }
            }
            User user = new User(loginUser.getUsername(),loginUser.getPassword(),authorityList);
            return user;
        }
        return null;
    }
}
