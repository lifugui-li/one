package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @PostMapping("/submit")
    public Result submitOrder(@RequestBody Map<String,Object> map){
        String validateCode = (String) map.get("validateCode");
        String telephone = (String)map.get("telephone");// 此处省略验证手机号码,手机号码不为空
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        String codeInRedis = jedis.get(key);
        if(null == codeInRedis){
            return new Result(false, "请重新获取验证码");
        }
        // redis中存有验证码,校验验证码
        if(!codeInRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 调用业务服务进行预约
        // 设置预约类型
        map.put("orderType","微信预约");
        try {
            Result result = orderService.addOrder(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }

    }

    @GetMapping("/findById")
    public Result findById(int id){
        Map<String,Object> data = orderService.findById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,data);
    }
}
