package com.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.PaperManage;

import java.io.File;
import java.util.List;

public interface PaperService {

    List<PaperManage> findAll();

    List<PaperManage> findById(Integer paperId);

    int add(PaperManage paperManage);

    File createWordFile(int tag ,int paperId) ;

    ApiResult comExam(int mulCount, int judCount, int fillCount, int level, int[] types, int paperId) ;
}
