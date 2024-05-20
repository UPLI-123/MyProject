package com.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.entity.Apply;
import com.exam.vo.ApplyPuls;
import io.swagger.models.auth.In;

/**
 * (Apply)表服务接口
 *
 * @author makejava
 * @since 2022-04-28 15:55:05
 */
public interface ApplyService{

    public int addApply(Integer uid ,Integer rid ) ;

    public Apply findByUid(Integer uid) ;

    public Integer delApply(Integer id) ;

    public IPage<ApplyPuls> findAll(Page page)  ;

    public Integer submitCheck(Integer id,String content,Integer tag) ;

}

