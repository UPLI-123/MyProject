package com.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.User;
import com.exam.entity.UserRole;
import com.exam.service.UserRoleService;
import com.exam.util.ApiResultHandler;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import java.io.Serializable;
import java.util.List;

/**
 * (UserRole)表控制层
 *
 * @author makejava
 * @since 2022-04-09 22:03:07
 */
@RestController
public class UserRoleController extends ApiController {

    @Autowired
    UserRoleService userRoleService ;

    @ApiOperation("改变用户的角色")
    @RequestMapping(value = "/updateRole",method = RequestMethod.POST)
    public ApiResult updateRole(@RequestBody User user){
        try{
            userRoleService.updateRole(user) ;
            return ApiResultHandler.success() ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"",null)  ;
        }
    }

}

