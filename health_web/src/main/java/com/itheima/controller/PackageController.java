package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.Util.QiNiuUtil;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.Result;
import com.itheima.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.itheima.pojo.Package;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/package")
public class PackageController {

    @Reference
    private PackageService packageService;

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        // 原文件名
        String originalFilename = imgFile.getOriginalFilename();
        // 文件的扩展名
        int lastIndexOf = originalFilename.lastIndexOf(".");
        String fileExtension = originalFilename.substring(lastIndexOf);
        // 生成新的文件名
        String newFilename = UUID.randomUUID() + fileExtension;
        try {
            // 调用七牛上传文件
            QiNiuUtil.uploadViaByte(imgFile.getBytes(), newFilename);
            // 存入redis缓存，所有上传的文件集合中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newFilename);
            // 图片的链接地址
            String imgUrl = QiNiuUtil.DOMAIN + "/" + newFilename;
            // 返回图片的链接地址， 回显图片
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,imgUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Package pkg, Integer[] checkgroupIds){
        packageService.add(pkg, checkgroupIds);
        // 存入redis中的保存到数据库的集合中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pkg.getImg());
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }
}
