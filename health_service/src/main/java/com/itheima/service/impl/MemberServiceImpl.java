package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 通过手机号码查询会员信息
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 添加新会员
     * @param member
     */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 会员数量统计
     * @return
     */
    @Override
    public Map<String, Object> getMemberReport() {
        // 上一年度的数量
        // 此时得到的是当前时间的日历
        Calendar car = Calendar.getInstance();
        // 运算成上一年
        car.add(Calendar.YEAR, -1);
        // 循环12个月
        List<String> months = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<Integer> counts = new ArrayList<Integer>();
        for (int i = 0; i < 12; i++){
            car.add(Calendar.MONTH,1);
            Date time = car.getTime();
            String monthDay = sdf.format(time);
            months.add(monthDay);
            // 调用Dao查询
            counts.add(memberDao.findMemberCountByDate(monthDay + "-31"));
        }
        // 构建返回的结果
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("months",months); // 图表 x轴的数据
        map.put("memberCount",counts ); // 系列中的数据
        return map;
    }

    @Override
    public List<Map<String, Object>> getSex() {
        return memberDao.getSex();
    }

    @Override
    public List<Map<String, Object>> getAge() {
        return memberDao.getAge();
    }

    public static void main(String[] args) {
        Calendar car = Calendar.getInstance();
        // 运算成上一年
        car.add(Calendar.YEAR, -1);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(car.getTime()));
    }
}
