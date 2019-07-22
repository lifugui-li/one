package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

public interface OrderService {
    /**
     * 预约
     * @param map
     * @return
     */
    Result addOrder(Map<String,Object> map)throws Exception;

    /**
     * 查询预约信息
     * @param id
     * @return
     */
    Map<String,Object> findById(int id);
}
