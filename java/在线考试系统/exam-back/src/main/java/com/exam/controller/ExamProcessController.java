package com.exam.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.ExamManage;
import com.exam.entity.ExamProcess;
import com.exam.service.ExamProcessService;
import com.exam.util.ApiResultHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.ibatis.javassist.util.proxy.ProxyObjectOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (ExamProcess)表控制层
 *
 * @author makejava
 * @since 2022-05-04 16:02:36
 */
@RestController
public class ExamProcessController {
    /**
     * 服务对象
     */
    @Autowired
    private ExamProcessService examProcessService;

    @ApiOperation("增加过程化考试")
    @RequestMapping(value = "/addExamP",method = RequestMethod.POST)
    public ApiResult addExamP(@RequestBody ExamProcess examProcess){
        try {
            System.out.println(examProcess.getBrief());
            System.out.println(examProcess.getCount());
            System.out.println(examProcess.getUid());
            examProcessService.addExamP(examProcess) ;
            return ApiResultHandler.success() ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("获得所有创建的过程化的考试")
    @RequestMapping(value = "/getAllProce" ,method = RequestMethod.POST)
    public ApiResult getAllProce(int id,int page,int size){
        try {
            Page<ExamProcess> page1 = new Page<>(page,size) ;
            IPage all = examProcessService.findByUid(page1, id);
            return ApiResultHandler.success(all) ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }

    }

    @ApiOperation("通过ID 获取当前 过程化试题的信息")
    @RequestMapping(value = "/getDetailProcess",method = RequestMethod.POST)
    public ApiResult getDetailProcess(Integer id){
        try {
            ExamProcess info = examProcessService.findById(id);
            return  ApiResultHandler.success(info) ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("修改过程化考试的信息")
    @RequestMapping(value = "/updateExP",method = RequestMethod.POST)
    public ApiResult updateExP(@RequestBody ExamProcess examProcess){
        try {
            examProcessService.updateExamP(examProcess) ;
            return ApiResultHandler.success() ;
        }catch (Exception e ){
            return ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("根据fatherid进行查询过程化成绩")
    @RequestMapping(value = "/getPlist",method = RequestMethod.POST)
    public ApiResult getPlist(Integer id){
        try {
            List<ExamManage> all = examProcessService.findByFid(id);
            return ApiResultHandler.success(all) ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }

}

