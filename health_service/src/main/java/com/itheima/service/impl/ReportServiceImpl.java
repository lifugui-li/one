package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.Util.DateUtils;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.PackageDao;
import com.itheima.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PackageDao packageDao;

    /**
     * 运营数据统计
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() {
        // 当前日期
        Date date = new Date();
        String today = DateUtils.parseDate2String(date, "yyyy-MM-dd");
        // 星期一
        String monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday(),"yyyy-MM-dd");
        // 1号
        String firstDayOfThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDayOfThisMonth(),"yyyy-MM-dd");
        // 星期天
        String sunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek(),"yyyy-MM-dd");
        // 本月最后一天
        String lastDayOfThisMonth = DateUtils.parseDate2String(DateUtils.getLastDayOfThisMonth(), "yyyy-MM-dd");

        // 本日新增会员数
        Integer todayNewMember = memberDao.findMemberCountAfterDate(today);
        // 会员总数
        Integer totalMember = memberDao.findMemberTotalCount();
        // 本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        // 本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);
        // 本日预约
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        // 本日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        // 本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday, sunday);
        // 本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        // 本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth,lastDayOfThisMonth);
        // 本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfThisMonth);
        // 热门套餐
        List<Map<String,Object>> hotPackage = packageDao.getHotPackages();

        // 返回给Controller, 封到map中
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("reportDate",today);
        resultMap.put("todayNewMember",todayNewMember);
        resultMap.put("totalMember",totalMember);
        resultMap.put("thisWeekNewMember",thisWeekNewMember);
        resultMap.put("thisMonthNewMember",thisMonthNewMember);
        resultMap.put("todayOrderNumber",todayOrderNumber);
        resultMap.put("todayVisitsNumber",todayVisitsNumber);
        resultMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        resultMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        resultMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        resultMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        resultMap.put("hotPackage",hotPackage);
        return resultMap;
    }
}
