package com.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ExamManage;
import com.exam.entity.Score;
import org.omg.CORBA.INTERNAL;

import java.util.List;

public interface ScoreService {
    int add(Score score);

    List<Score> findAll();

    IPage<Score> findById(Page page, Integer studentId);

    List<Score> findById(Integer studentId);

    List<Score> findByExamCode(Integer examCode);

    int pass(Integer id,Integer uid,Integer examCode) ;

    int checkPower(Integer uid, Integer examCode) ;

    String findImg(Integer uid,Integer examCode) ;

    List<ExamManage> getAllByUid(int id) ;

    List<ExamManage> findByTime(int id, String time) ;
}
