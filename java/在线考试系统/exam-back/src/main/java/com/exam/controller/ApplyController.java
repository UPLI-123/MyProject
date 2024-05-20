package com.exam.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.Apply;
import com.exam.entity.Teacher;
import com.exam.service.ApplyService;
import com.exam.util.ApiResultHandler;
import com.exam.vo.ApplyPuls;
import io.swagger.annotations.ApiOperation;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Apply)表控制层
 *
 * @author makejava
 * @since 2022-04-28 15:55:05
 */
@RestController
public class ApplyController  {
    /**
     * 服务对象
     */
    @Autowired
    private ApplyService applyService;

    @ApiOperation("提交申请")
    @RequestMapping(value = "/submitApply",method = RequestMethod.POST)
    public ApiResult submitApply(Integer uid, Integer rid) {
        try{
//            System.out.println("111"+uid);
//            System.out.println("222"+rid);
            applyService.addApply(uid,rid) ;
            return ApiResultHandler.success() ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"请求失败",null) ;
        }
    }

    @ApiOperation("获取当前用户的申请信息")
    @RequestMapping(value = "/getCurApply",method = RequestMethod.POST)
    public ApiResult getCurApply(Integer uid) {
        try {
            Apply info = applyService.findByUid(uid);
            return ApiResultHandler.success(info) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"",null) ;
        }
    }

    @ApiOperation("删除当前的申请")
    @RequestMapping(value = "/delApply",method = RequestMethod.POST)
    public ApiResult delApply(Integer id){
        try{
            applyService.delApply(id) ;
            return ApiResultHandler.success() ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"",null) ;
        }
    }

    @ApiOperation("获得申请表的所有信息,分页查询")
    @RequestMapping(value = "/getApply",method = RequestMethod.POST)
    public ApiResult getApply(Integer cur,Integer size){
        try {
            Page page = new Page(cur,size) ;
            IPage<ApplyPuls> all = applyService.findAll(page);
            return ApiResultHandler.success(all) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"",null) ;
        }
    }

    @ApiOperation("提交审核信息操作")
    @RequestMapping(value = "/submitCheck",method = RequestMethod.POST)
    public ApiResult submitCheck(Integer id,String content,Integer tag){
        try{
//            System.out.println("111"+id);
            applyService.submitCheck(id,content, tag)  ;
            return ApiResultHandler.success() ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"",null) ;
        }
    }



}

