package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.Util.QiNiuUtil;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Package;
import com.itheima.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RestController
@RequestMapping("/package")
public class PackageController {

    @Reference
    private PackageService packageService;

    @Autowired
    private JedisPool jedisPool;


    @GetMapping("/getPackage")
    public Result getPackage(){
        // 调用业务服务获取套餐列表
        List<Package> list = packageService.findAll();
        // 拼接图片的链接地址
        list.forEach(pkg->{
            String imageUrl = QiNiuUtil.DOMAIN + "/" + pkg.getImg();
            pkg.setImg(imageUrl);
        });
        /*for (Package pkg : list) {
            String imageUrl = QiNiuUtil.DOMAIN + "/" + pkg.getImg();
            pkg.setImg(imageUrl);
        }*/
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,list);
    }

    @GetMapping("/findById")
    public Result findById(int id){
        Package pkg = packageService.findById(id);
        pkg.setImg(QiNiuUtil.DOMAIN + "/" + pkg.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,pkg);
    }

    @GetMapping("/findPackageById")
    public Result findPackageById(int id){
        Package pkg = packageService.findPackageById(id);
        pkg.setImg(QiNiuUtil.DOMAIN + "/" + pkg.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,pkg);
    }



}
