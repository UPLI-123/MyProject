package com.exam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.MultiQuestion;
import com.exam.serviceimpl.MultiQuestionServiceImpl;
import com.exam.util.ApiResultHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin // 解决跨域问题
public class MultiQuestionController {

    @Autowired
    private MultiQuestionServiceImpl multiQuestionService;
    @GetMapping("/multiQuestionId")
    public ApiResult findOnlyQuestion() {
        MultiQuestion res = multiQuestionService.findOnlyQuestionId();
        return ApiResultHandler.buildApiResult(200,"查询成功",res);
    }
    @ApiOperation("向题库中添加选择题")
    @PostMapping("/MultiQuestion")
    public ApiResult add(@RequestBody MultiQuestion multiQuestion) {
        int res = multiQuestionService.add(multiQuestion);
        if (res != 0) {

            return ApiResultHandler.buildApiResult(200,"添加成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"添加失败",res);
    }
    @ApiOperation("查询当前题库的所有的选择题")
    @RequestMapping(value = "/SelectAllMul" , method = RequestMethod.POST)
    public ApiResult slectAllMul(Integer eid,Integer page,Integer size){
        Page<MultiQuestion> page1 = new Page(page,size) ;
        IPage<MultiQuestion> all = multiQuestionService.findAll(page1, eid);
        return ApiResultHandler.buildApiResult(200,"查询成功",all) ;
    }
    @ApiOperation("在当前题库中删除相应的题目")
    @RequestMapping(value = "/delMul",method = RequestMethod.POST)
    public ApiResult delMul(Integer id,Integer eid) {
        try{
//           根据id 进行删除操作
             multiQuestionService.removeMul(id,eid);
             return ApiResultHandler.buildApiResult(200,"删除成功",null) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"删除失败",null) ;
        }
    }

    @ApiOperation("对每一道题的内容的更新")
    @RequestMapping(value = "/updateMul",method = RequestMethod.POST)
    public ApiResult updateMul(@RequestBody  MultiQuestion multiQuestion){
        try{
            System.out.println("11" + multiQuestion);
            multiQuestionService.updateMul(multiQuestion);
            return ApiResultHandler.buildApiResult(200,"修改成功",null) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"修改失败",null) ;
        }
    }

//    @ApiOperation("word文档导入题库")
//    @RequestMapping(value = "/uploadWord",method = RequestMethod.POST)
//    public ApiResult uploadWord(HttpServletRequest request, Integer examid, Integer studentid){
//        try{
//
//            return ApiResultHandler.buildApiResult(200,"修改成功",null) ;
//        }catch (Exception e){
//            return ApiResultHandler.buildApiResult(500,"上传失败",null) ;
//        }
//    }
}
