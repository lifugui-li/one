package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.Util.POIUtils;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @PostMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        // 读取excel
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            // 转成Ordersetting对象
            OrderSetting os = null;
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            List<OrderSetting> orderSettings = new ArrayList<OrderSetting>();
            for (String[] strArr : list) {
                // 创建预约设置的对象
                os = new OrderSetting(sdf.parse(strArr[0]),Integer.valueOf(strArr[1]));
                orderSettings.add(os);
            }
            // 调用业务
            orderSettingService.add(orderSettings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        }catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }

    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        List<OrderSetting> datas = orderSettingService.getOrderSettingByMonth(month);
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, datas);
    }

    @PostMapping("/setOrderSetting")
    public Result setOrderSetting(@RequestBody OrderSetting orderSetting){
        orderSettingService.updateOrderSetting(orderSetting);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}
