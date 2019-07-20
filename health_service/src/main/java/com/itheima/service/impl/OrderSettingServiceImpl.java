package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettings) {
        /**
         * - 判断是否已经设置过了，通过日期查询 where orderDate =导入的日期
         *
         * - 对已经导入过的，实现更新数量
         * -
         */
        for (OrderSetting orderSetting : orderSettings) {
            //判断是否已经设置过了，通过日期查询 where orderDate =导入的日期
            OrderSetting os = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
            if(null != os){
                // 对已经导入过的，实现更新数量
                orderSettingDao.updateNumber(orderSetting);
            }else{
                // 没导入过就调用Dao插入到数据库
                orderSettingDao.add(orderSetting);
            }
        }
    }

    /**
     * 按月份查询预约设置信息
     * @param month
     * @return
     */
    @Override
    public List<OrderSetting> getOrderSettingByMonth(String month) {
        // 月份的第一天
        String startDate = month + "-01";
        String endDate = month + "-31";
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(startDate, endDate);
        return list;
    }

    /**
     * 设置预约设置信息
     * @param orderSetting
     */
    @Override
    @Transactional
    public void updateOrderSetting(OrderSetting orderSetting) {
        //判断是否已经设置过了，通过日期查询 where orderDate =导入的日期
        OrderSetting os = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        if(null != os){
            // 对已经导入过的，实现更新数量
            orderSettingDao.updateNumber(orderSetting);
        }else{
            // 没导入过就调用Dao插入到数据库
            orderSettingDao.add(orderSetting);
        }
    }
}
