package com.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.entity.EaxmStore;

import java.util.List;

/**
 * (EaxmStore)表服务接口
 *
 * @author makejava
 * @since 2022-04-14 10:08:13
 */
public interface EaxmStoreService {

    public void addExamStore(EaxmStore eaxmStore) ;

    public IPage<EaxmStore> findExamStore(Page page, Integer id) ;

    public int delById(int id) ;

    public List<EaxmStore> findByUid(Integer id) ;

}

