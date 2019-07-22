package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    /**
     * 批量导入
     * @param orderSettings
     */
    void add(List<OrderSetting> orderSettings);

    /**
     * 按月份查询预约设置信息
     * @param month
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(String month);

    /**
     * 设置预约设置信息
     * @param orderSetting
     */
    void updateOrderSetting(OrderSetting orderSetting);
}
