package com.itheima.controller;

import com.itheima.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 拦截controller
 */
@ControllerAdvice
public class GloableExceptionHandler {
    /**
     * 定义log日志
     */
    private static final Logger log = LoggerFactory.getLogger(GloableExceptionHandler.class);

//    拦截的异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result handlerExcepiton(RuntimeException e){
        log.error("出错了",e);
        //要响应给前端
        return new Result(false, e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handlerExcepiton(Exception e){
        log.error("出错了",e);
        //要响应给前端
        return new Result(false, "执行出错了");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result handlerAccessDeniedException(AccessDeniedException e){
        log.error("出错了",e);
        //要响应给前端
        return new Result(false, "权限不足");
    }
}
