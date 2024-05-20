package com.exam.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.entity.ExamManage;
import com.exam.mapper.EaxmStoreMapper;
import com.exam.entity.EaxmStore;
import com.exam.service.EaxmStoreService;
import com.exam.util.TimeConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * (EaxmStore)表服务实现类
 *
 * @author makejava
 * @since 2022-04-14 10:08:13
 */
@Service
public class EaxmStoreServiceImpl  implements EaxmStoreService {

    @Autowired
    EaxmStoreMapper eaxmStoreMapper   ;

    @Override
    public void addExamStore(EaxmStore examStore) {

        System.out.println(examStore);

    //    设置传过来的默认值
    //    获得当前的时间 ，然后利用时间转化函数 转化为指定的时间
        Date date = new Date(); // 获得当前的时间
        examStore.setGtime(TimeConvert.convert(date));
        examStore.setCount(0);
        eaxmStoreMapper.addExamStore(examStore);
    }

    @Override
    public IPage<EaxmStore> findExamStore(Page page, Integer id) {
        return eaxmStoreMapper.selectAll(page,id) ;
    }

//     根据id 来删除题库
    @Override
    public int delById(int id) {
//        只是进行题库的删除，并不会对相应的题目就行删除 因为会影响到相应的
        return eaxmStoreMapper.delById(id);
    }

    @Override
    public List<EaxmStore> findByUid(Integer id) {
        return eaxmStoreMapper.findByUid(id);
    }
}

