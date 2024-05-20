package com.exam.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.EmailInfo;
import com.exam.service.EmailInfoService;
import com.exam.util.ApiResultHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (EmailInfo)表控制层
 *
 * @author LCH
 * @since 2022-04-12 16:18:17
 */
@RestController
public class EmailInfoController extends ApiController {

    @Autowired
    EmailInfoService emailInfoService ;

    @ApiOperation("向邮箱发送信息")
    @RequestMapping(value = "/sendEmail",method = RequestMethod.POST)
    public ApiResult send(@RequestBody EmailInfo emailInfo){
        return  emailInfoService.send(emailInfo);
    }



}

