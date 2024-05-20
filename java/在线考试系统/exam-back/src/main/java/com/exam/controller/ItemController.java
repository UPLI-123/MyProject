package com.exam.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.GA.GeneticAlgorithm;
import com.exam.entity.ApiResult;
import com.exam.entity.PaperManage;
import com.exam.service.PaperService;
import com.exam.serviceimpl.FillQuestionServiceImpl;
import com.exam.serviceimpl.JudgeQuestionServiceImpl;
import com.exam.serviceimpl.MultiQuestionServiceImpl;
import com.exam.util.ApiResultHandler;
import com.exam.vo.GAquestion;
import com.exam.vo.Item;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin // 解决跨域问题
public class ItemController {

    @Autowired
    MultiQuestionServiceImpl multiQuestionService;

    @Autowired
    FillQuestionServiceImpl fillQuestionService;

    @Autowired
    JudgeQuestionServiceImpl judgeQuestionService;

    @Autowired
    PaperService paperService;

    @ApiOperation("遗传算法智能组卷")
    @PostMapping("/item")
    public ApiResult ItemController(@RequestBody Item item) {
        // 获取全部的信息
        // 选择题
        Integer mulCount = item.getChangeNumber();
        // 填空题
        Integer fillCount = item.getFillNumber();
        // 判断题
        Integer judCount = item.getJudgeNumber();
        //出卷id
        Integer paperId = item.getPaperId();
        System.out.println("111:"+paperId);
        Integer level = item.getLevel();
        int[] types = item.getValue();
        return paperService.comExam(mulCount,judCount,fillCount,level,types,paperId) ;
    }
}
