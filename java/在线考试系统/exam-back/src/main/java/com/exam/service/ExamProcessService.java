package com.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.entity.ExamManage;
import com.exam.entity.ExamProcess;

import java.util.List;

/**
 * (ExamProcess)表服务接口
 *
 * @author makejava
 * @since 2022-05-04 16:02:37
 */
public interface ExamProcessService {

    public int addExamP(ExamProcess process) ;

    public IPage  findByUid(Page page,Integer id) ;

    public ExamProcess findById(int id) ;

    public int  updateExamP(ExamProcess examProcess) ;

    public List<ExamManage> findByFid(Integer fid) ;

}

