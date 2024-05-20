package com.exam.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.dao.MBlogMapper;
import com.exam.entity.MBlog;
import com.exam.service.MBlogService;
import com.exam.util.TimeConvert;
import com.sun.scenario.animation.shared.TimelineClipCore;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * (MBlog)表服务实现类
 *
 * @author makejava
 * @since 2022-05-12 11:14:48
 */
@Service
public class MBlogServiceImpl implements MBlogService {

    @Autowired
    MBlogMapper mBlogMapper ;

    @Override
    public IPage<MBlog> findByExamCode(Page page,int examCode, int status) {
        return mBlogMapper.findBlogsByExamcode(page,examCode,status);
    }

    @Override
    public int editBlog(MBlog mBlog) {
//        判断一下是新增 还是修改操作
        Integer id = mBlog.getId();
//        计算出来当前的时间
        Date date = new Date() ;
        String time = TimeConvert.convert(date);
        mBlog.setCreated(time);
        if(id == null){ // 执行新增操作
//            新增操作的话 默认是 0
            mBlog.setStatus(0)  ;
            return mBlogMapper.addBlog(mBlog) ;
        }else {
//            实践上，要修改的只有文章的标题 内容 摘要 和 时间
            return mBlogMapper.updateById(mBlog);
        }
    }

    @Override
    public MBlog findById(int id) {
        return mBlogMapper.findById(id);
    }
}

