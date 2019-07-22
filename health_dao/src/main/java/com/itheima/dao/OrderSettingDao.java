package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    /**
     * 通过日期查询预约设置
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     * 更新可预约数量
     * @param orderSetting
     */
    void updateNumber(OrderSetting orderSetting);

    /**
     * 添加预约设置
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 按月份查询预约设置信息
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    //更新已预约人数
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
