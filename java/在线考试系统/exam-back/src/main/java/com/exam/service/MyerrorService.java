package com.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.entity.FillQuestion;
import com.exam.entity.JudgeQuestion;
import com.exam.entity.MultiQuestion;
import com.exam.entity.Myerror;

import java.util.List;

/**
 * (Myerror)表服务接口
 *
 * @author makejava
 * @since 2022-05-10 14:54:32
 */
public interface MyerrorService {

//    查询出来当前用户的错题集

    public List<MultiQuestion> findErrorMul(int uid,int paperId) ;

    public List<JudgeQuestion> findErrorJud(int uid,int paperId) ;

    public List<FillQuestion> findErrorFill(int uid,int paperId) ;

    public int addMyError(Myerror myerror) ;

    public int removeMyError(int id) ;




}

