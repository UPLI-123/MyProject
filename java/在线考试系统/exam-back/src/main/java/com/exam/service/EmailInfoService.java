package com.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.entity.ApiResult;
import com.exam.entity.EmailInfo;

/**
 * (EmailInfo)表服务接口
 *
 * @author makejava
 * @since 2022-04-12 16:18:17
 */
public interface EmailInfoService {

    ApiResult send(EmailInfo emailInfo) ;

    EmailInfo selectEmailInfo(String toForm,String code) ;

    int deleteEmail(Integer id) ;


}

