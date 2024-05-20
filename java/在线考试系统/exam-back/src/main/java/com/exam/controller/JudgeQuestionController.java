package com.exam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.JudgeQuestion;
import com.exam.serviceimpl.JudgeQuestionServiceImpl;
import com.exam.util.ApiResultHandler;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin // 解决跨域问题
public class JudgeQuestionController {

    @Autowired
    private JudgeQuestionServiceImpl judgeQuestionService;
    @ApiOperation("向题库中添加判断题")
    @PostMapping("/judgeQuestion")
    public ApiResult add(@RequestBody JudgeQuestion judgeQuestion) {
        int res = judgeQuestionService.add(judgeQuestion);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200,"添加成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"添加失败",res);
    }
    @GetMapping("/judgeQuestionId")
    public ApiResult findOnlyQuestionId() {
        JudgeQuestion res = judgeQuestionService.findOnlyQuestionId();
        return  ApiResultHandler.buildApiResult(200,"查询成功",res);
    }
    @ApiOperation("查询当前题库中的全部法判断题")
    @RequestMapping(value = "/SelectAllJud",method = RequestMethod.POST)
    public ApiResult SelectAllJud(Integer eid, Integer page, Integer size){
        try{
            Page<JudgeQuestion> page1 = new Page(page,size) ;
            IPage<JudgeQuestion> all = judgeQuestionService.findAll(page1,eid);
            return ApiResultHandler.buildApiResult(200,"查询成功",all) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"查询失败",null) ;
        }
    }
    @ApiOperation("删除当前题库的判断题")
    @RequestMapping(value = "/delJud",method = RequestMethod.POST)
    public ApiResult delJud(Integer id,Integer eid) {
        try{
            judgeQuestionService.removeJud(id,eid);
            return ApiResultHandler.buildApiResult(200,"删除成功",null) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"删除失败",null)  ;
        }
    }

    @ApiOperation("更新当前题库的判断题")
    @RequestMapping(value = "/updateJud" ,method = RequestMethod.POST)
    public ApiResult updateJud(@RequestBody JudgeQuestion judgeQuestion){
        try{
            judgeQuestionService.updateJud(judgeQuestion);
            return ApiResultHandler.buildApiResult(200,"修改成功",null) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"修改失败",null) ;
        }
    }
}
