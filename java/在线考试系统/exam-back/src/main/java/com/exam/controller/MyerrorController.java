package com.exam.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.*;
import com.exam.service.MyerrorService;
import com.exam.util.ApiResultHandler;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Myerror)表控制层
 *
 * @author makejava
 * @since 2022-05-10 14:54:32
 */
@CrossOrigin
@RestController
@RequestMapping
public class MyerrorController {
    /**
     * 服务对象
     */
    @Autowired
    private MyerrorService myerrorService;

    @ApiOperation("查询当前试题的错题本中的选择题")
    @RequestMapping(value = "/findErrorMul",method = RequestMethod.POST)
    public ApiResult findErrorMul(int uid, int pid){
        try {
            List<MultiQuestion> all = myerrorService.findErrorMul(uid, pid);
            return ApiResultHandler.success(all) ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("查询当前试题中错题中的判断题")
    @RequestMapping(value = "/findErrorJud" ,method = RequestMethod.POST)
    public ApiResult findErrorJud(int uid,int pid){
        try{
            List<JudgeQuestion> all = myerrorService.findErrorJud(uid, pid);
            return ApiResultHandler.success(all) ;
        }catch (Exception e){
            return ApiResultHandler.fail();
        }
    }

    @ApiOperation("查询当前试题中的填空题")
    @RequestMapping(value = "/findErrorFill",method = RequestMethod.POST)
    public ApiResult findErrorFill(int uid,int pid ){
        try{
            List<FillQuestion> all = myerrorService.findErrorFill(uid, pid);
            return ApiResultHandler.success(all);
        }catch (Exception e){
            return  ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("添加到错题中")
    @RequestMapping(value = "/addError" ,method = RequestMethod.POST)
    public ApiResult addError(int type ,int paperId,int examCode ,int uid,int questionId ){
        try{
            Myerror myerror = new Myerror() ;
            myerror.setPaperid(paperId) ;
            myerror.setType(type);
            myerror.setExamcode(examCode);
            myerror.setQuestionId(questionId);
            myerror.setUid(uid);
            System.out.println(myerror);
            myerrorService.addMyError(myerror) ;
            return  ApiResultHandler.success() ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("移除当前的错题")
    @RequestMapping(value = "/removeError",method = RequestMethod.POST)
    public ApiResult removeError(int id){
        try{
            myerrorService.removeMyError(id) ;
            return ApiResultHandler.success() ;
        }catch (Exception e){
            return ApiResultHandler.fail()  ;
        }
    }

}

