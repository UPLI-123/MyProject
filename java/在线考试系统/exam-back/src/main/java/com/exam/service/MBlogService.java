package com.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.entity.MBlog;
import org.apache.tomcat.util.modeler.modules.MbeansDescriptorsDigesterSource;

/**
 * (MBlog)表服务接口
 *
 * @author makejava
 * @since 2022-05-12 11:14:48
 */
public interface MBlogService {

    IPage<MBlog> findByExamCode(Page page,int examCode, int status) ;

    int editBlog(MBlog mBlog) ;

    MBlog findById(int  id);



}

