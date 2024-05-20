package com.exam.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.Mylist;
import com.exam.service.MylistService;
import com.exam.util.ApiResultHandler;
import io.swagger.annotations.ApiOperation;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Mylist)表控制层
 *
 * @author makejava
 * @since 2022-05-09 15:51:19
 */
@RestController
public class MylistController {
    /**
     * 服务对象
     */
    @Autowired
    private MylistService mylistService;

    @ApiOperation("新增代办事项")
    @RequestMapping(value = "/addMylist",method = RequestMethod.POST)
    public ApiResult addMylist(@RequestBody Mylist mylist){
        try {
            mylistService.addList(mylist) ;
            return ApiResultHandler.success() ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("查询所有当天所有的事务")
    @RequestMapping(value = "/getAllList",method = RequestMethod.POST)
    public ApiResult getAllList(String time){
        try {
            List<Mylist> all = mylistService.findAllList(time);
            return ApiResultHandler.success(all) ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("删除当前的事务")
    @RequestMapping(value = "/delList",method = RequestMethod.POST)
    public ApiResult delList(int id){
        try{
            mylistService.delById(id) ;
            return ApiResultHandler.success() ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("修改当前事务的状态")
    @RequestMapping(value = "/updatePower",method = RequestMethod.POST)
    public ApiResult updatePower(int id ,int power){
        try{
            mylistService.updateListPower(id,power) ;
            return ApiResultHandler.success() ;
        }catch (Exception e){
            return  ApiResultHandler.fail() ;
        }
    }



}

