package com.exam.exception;

import com.exam.entity.ApiResult;
import com.exam.util.ApiResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @projectName: exam
 * @package: com.exam.exception
 * @className: GlobalException
 * @author: lch
 * @description: 对全局异常的处理
 * @date: 2022/4/10 8:55
 * @version: 1.0
 */
@RestControllerAdvice //  是对Controller进行增强的，可以全局捕获spring mvc抛的异常
@Slf4j
public class GlobalException {

    @ExceptionHandler(value = UnauthorizedException.class)
    public ApiResult hander(UnauthorizedException e){
        log.error("无访问权限------{}", e.getMessage());
        return ApiResultHandler.buildApiResult(401,"无访问权限",null) ;

    }

    @ExceptionHandler(value = ExpiredCredentialsException.class)
    public ApiResult handler(ExpiredCredentialsException e) {
        log.error("登录已过期：----------------{}", e.getMessage());
        return ApiResultHandler.buildApiResult(401,"登录已过期，请重新登录",null) ;
    }


    @ExceptionHandler(value = UnauthenticatedException.class)
    public ApiResult handler(UnauthenticatedException e) {
        log.error("运行时异常：----------------{}", e);
        return ApiResultHandler.buildApiResult(401,"未登录",null)  ;
    }

    @ExceptionHandler(value = UnknownAccountException.class)
    public ApiResult handler(UnknownAccountException e) {
        log.error("运行时异常：----------------{}", e);
        return ApiResultHandler.buildApiResult(401,"找到不到账号",null) ;
    }



}
