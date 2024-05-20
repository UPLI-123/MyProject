package com.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.Permission;
import com.exam.service.PermissionService;
import com.exam.util.ApiResultHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * (Permission)表控制层
 *
 * @author makejava
 * @since 2022-04-09 22:03:09
 */
@RestController
public class PermissionController extends ApiController {

    @Autowired
    private PermissionService permissionService ;

    @ApiOperation("获得树形的权限管理")
    @RequestMapping(value = "/getAllPermission", method = RequestMethod.GET)
    public ApiResult getAllPermission(){

        try{
            List<Map<String, Object>> tree = permissionService.findTree();
            return  ApiResultHandler.success(tree) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"查询失败",null) ;
        }
    }

    @ApiOperation("树形主键初始化方法")
        @RequestMapping(value = "/initTree",method = RequestMethod.POST)
    public ApiResult initTree(Integer id){
        try{
            // todo 初始化方法
            Map<String, Object> map = permissionService.initTree(id);
            return ApiResultHandler.success(map) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"初始化失败",null) ;
        }

    }


}

