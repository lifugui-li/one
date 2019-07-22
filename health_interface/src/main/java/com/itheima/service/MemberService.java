package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    /**
     * 通过手机号码查询会员信息
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加新会员
     * @param member
     */
    void add(Member member);

    /**
     * 会员数量统计
     * @return
     */
    Map<String, Object> getMemberReport();

    List<Map<String,Object>> getSex();

    List<Map<String,Object>> getAge();
}
