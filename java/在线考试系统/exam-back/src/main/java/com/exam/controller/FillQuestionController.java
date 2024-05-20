package com.exam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.FillQuestion;
import com.exam.serviceimpl.FillQuestionServiceImpl;
import com.exam.util.ApiResultHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin // 解决跨域问题
public class FillQuestionController {

    @Autowired
    private FillQuestionServiceImpl fillQuestionService;

    @ApiOperation("向题库中添加填空题")
    @PostMapping("/fillQuestion")
    public ApiResult add(@RequestBody FillQuestion fillQuestion) {
        int res = fillQuestionService.add(fillQuestion);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200,"添加成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"添加失败",res);
    }

    @GetMapping("/fillQuestionId")
    public ApiResult findOnlyQuestionId() {
        FillQuestion res = fillQuestionService.findOnlyQuestionId();
        return ApiResultHandler.buildApiResult(200,"查询成功",res);
    }

    @ApiOperation("获得当前题库的填空题")
    @RequestMapping(value = "/SelectAllFill", method = RequestMethod.POST)
    public ApiResult SelectAllFill(Integer eid ,Integer page, Integer size){
        try{
            Page<FillQuestion> page1 = new Page(page,size) ;
            IPage<FillQuestion> all = fillQuestionService.findAll(page1, eid);
            return ApiResultHandler.buildApiResult(200,"查询成功",all) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"查询失败",null)  ;
        }
    }
    @ApiOperation("删除当前题库中填空题")
    @RequestMapping(value = "/delFill",method = RequestMethod.POST)
    public ApiResult delFill(Integer id,Integer eid){
        try{
            fillQuestionService.removeFill(id,eid);
            return  ApiResultHandler.buildApiResult(200,"删除成功",null);
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"删除失败",null) ;
        }
    }

    @ApiOperation("更改当前题库中的填空题")
    @RequestMapping(value = "/updateFill",method = RequestMethod.POST)
    public ApiResult updateFill(@RequestBody FillQuestion fillQuestion){
        try{
            fillQuestionService.updateFill(fillQuestion);
            return ApiResultHandler.buildApiResult(200,"添加成功",null) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"修改失败",null) ;
        }

    }
}
