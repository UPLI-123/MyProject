package com.exam.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.Role;
import com.exam.entity.RolePermission;
import com.exam.mapper.RolePermissionMapper;
import com.exam.service.RolePermissionService;
import com.exam.util.ApiResultHandler;
import com.exam.vo.Roots;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (RolePermission)表控制层
 *
 * @author makejava
 * @since 2022-04-09 22:03:09
 */
@RestController
public class RolePermissionController extends ApiController {

    @Autowired
    RolePermissionService rolePermissionService ;

    @ApiOperation("对用户的权限信息进行处理")
    @RequestMapping(value = "/dealRoleP",method = RequestMethod.POST)
    public ApiResult dealRoleP(Integer roleId,String roots){
        try{
//            System.out.println(roleId);
//            将前台 传过来的json字符串转化为int 型的数组
            JSONArray json = JSONObject.parseArray(roots);
//            创建一个 接收的数组
            Integer[] root = new Integer[json.size()] ;
//            json.stream().forEach(System.out::println);
            Integer [] arry = json.toArray(root) ;
//            从前端获得了数据 ，然后进行处理
            rolePermissionService.dealRoleP(roleId,arry) ;
            return ApiResultHandler.success() ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"失败",null) ;
        }
    }

}

