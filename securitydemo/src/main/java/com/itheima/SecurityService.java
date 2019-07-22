package com.itheima;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userService")
public class SecurityService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库获取用户信息, 通过登陆名查询
        User user = findUserByUname(username);
        // 返回UserDetails的对象，才表示验证通过
        // String username, 登陆用户名
        // String password, 登陆用户的密码
        // Collection<? extends GrantedAuthority> authorities 登陆用户权限集合 ROLE_ADMIN
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        // String role
        SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_ADMIN");
        authorities.add(sga);
        sga = new SimpleGrantedAuthority("add");
        authorities.add(sga);

        org.springframework.security.core.userdetails.User  userDetail =
                new org.springframework.security.core.userdetails.User(username, user.getPassword(),authorities);

        // 如果返回null，就代表没有通过验证
        return userDetail;
    }

    //模拟从数据库查询
    private User findUserByUname(String username) {
        if ("admin".equals(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword("$2a$10$./FSTtYmwEe02UTclJIZW.5RkVDv5od4/TM7cN0lOkRzOgrq6jLYi");
            return user;
        }
        return null;
    }
}
