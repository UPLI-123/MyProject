package com.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ExamManage;
import io.swagger.models.auth.In;

import java.util.List;

public interface ExamManageService {

    /**
     * 不分页查询所有考试信息
     */
    List<ExamManage> findAll(Integer id);
//    分页查询所有信息
    IPage<ExamManage> findAll(Page<ExamManage> page,Integer id);

    ExamManage findById(Integer examCode);

    int delete(Integer examCode);

    int update(ExamManage exammanage);

    int add(ExamManage exammanage);

    ExamManage findOnlyPaperId();
//     分页查询所有功能试卷
    IPage<ExamManage> findAll(Page<ExamManage> page);

    int updateIsPublic(Integer id, Integer power)  ;

    IPage<ExamManage> findWSH(Page page) ;

    int passExam(int createId,String content,int examCode,int tag) ;


}
