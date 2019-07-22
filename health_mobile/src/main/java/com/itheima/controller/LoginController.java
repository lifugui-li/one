package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @PostMapping("/check")
    public Result check(@RequestBody Map<String,String> paramMap, HttpServletResponse res){
        Jedis jedis = jedisPool.getResource();
        //- 验证 验证码
        // 获取手机号码
        String telephone = paramMap.get("telephone");
        String redisKey = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        String codeInRedis = jedis.get(redisKey);
        if(null == codeInRedis){
            return new Result(false, "请重新获取验证码");
        }
        if(!codeInRedis.equals(paramMap.get("validateCode"))){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //        - 验证通过 判断是否为会员
        Member member = memberService.findByTelephone(telephone);
        //        - 是 通过登陆，
        if(null == member) {
            //- 不是会员，添加新会员
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        //        - 写入Cookie，跟踪用户 跟用户的手机号码
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        // 一个月的有效期，单位是秒
        cookie.setMaxAge(60 * 60 * 24 * 30);
        // 网站的根目录, 用户访问哪个路径时才会带上这个cookie
        cookie.setPath("/");
        res.addCookie(cookie);
        //        - 返回结果给前端
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
