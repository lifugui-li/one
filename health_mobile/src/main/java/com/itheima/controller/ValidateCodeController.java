package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.Util.SMSUtils;
import com.itheima.Util.ValidateCodeUtils;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import sun.plugin2.message.Message;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/send4Order")
    public Result send4Order(String telephone ){
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        //判断是否发送过了，通过key(手机号码)获取redis中的验证吗
        Jedis jedis = jedisPool.getResource();
        // redis中的验证码
        String codeInRedis = jedis.get(key);
        if(!StringUtils.isEmpty(codeInRedis)){
            return new Result(false, MessageConstant.SENT_VALIDATECODE);
        }
        //不存在，生成验证码，再发送，存入redis中设置有效期(5mins)
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
            // 存入redis
            jedis.setex(key, 5*60, code + "");
            //返回结果给前端
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
    }

    @PostMapping("/send4Login")
    public Result send4Login(String telephone){
        // 判断是否已经发过了
        // 看redis中是否存在
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        if(null != jedis.get(key)){
            // 发送了
            return new Result(false, MessageConstant.SENT_VALIDATECODE);
        }
        // 生成 验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        return sendValidateCode(jedis,telephone,code + "", key);
    }

    private Result sendValidateCode(Jedis jedis, String telephone, String code, String key){
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
            // 存入redis
            jedis.setex(key, 5*60, code);
            //返回结果给前端
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
    }
}
