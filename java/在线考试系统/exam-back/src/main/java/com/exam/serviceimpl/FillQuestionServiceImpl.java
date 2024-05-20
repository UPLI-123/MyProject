package com.exam.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.EaxmStore;
import com.exam.entity.FillQuestion;
import com.exam.mapper.EaxmStoreMapper;
import com.exam.mapper.FillQuestionMapper;
import com.exam.service.FillQuestionService;
import com.exam.util.TimeConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class FillQuestionServiceImpl implements FillQuestionService {

    @Autowired
    private FillQuestionMapper fillQuestionMapper;

    @Autowired
    private EaxmStoreMapper eaxmStoreMapper ;

    @Override
    public List<FillQuestion> findByIdAndType(Integer paperId) {
        return fillQuestionMapper.findByIdAndType(paperId);
    }

    @Override
    public IPage<FillQuestion> findAll(Page<FillQuestion> page,Integer eid) {
        return fillQuestionMapper.findAll(page,eid);
    }

    @Override
    public FillQuestion findOnlyQuestionId() {
        return fillQuestionMapper.findOnlyQuestionId();
    }

//     因为涉及两个表之间的操作 ，所以要添加一些 事务的操作来保证事务的一致性

    @Override
    @Transactional
    public int add(FillQuestion fillQuestion) {
//         向题库 中 添加信息
        int add = fillQuestionMapper.add(fillQuestion);
//        更改题库的数量和时间
        EaxmStore eaxmStore = eaxmStoreMapper.selectById(fillQuestion.getEid());
//        准备更新时间
        Date date = new Date() ;
        String time = TimeConvert.convert(date);
        eaxmStore.setGtime(time);
        eaxmStore.setCount(eaxmStore.getCount()+1);
//        进行更新操作
        int add1 = eaxmStoreMapper.updateStore(eaxmStore);
        return (add & add1) ;
    }

    @Override
    public List<Integer> findBySubject(String subject, Integer pageNo) {
        return fillQuestionMapper.findBySubject(subject,pageNo);
    }

    @Override
    public void removeFill(Integer id,Integer eid) {
        fillQuestionMapper.delFill(id);
        //        更新题库的数量
        EaxmStore eaxmStore = new EaxmStore() ;
        eaxmStore.setId(eid)  ;
        Date date = new Date() ;
        String time = TimeConvert.convert(date);
        eaxmStore.setGtime(time);
        eaxmStoreMapper.updateCount(eaxmStore) ;
    }

    @Override
    public void updateFill(FillQuestion fillQuestion) {
        fillQuestionMapper.updteFill(fillQuestion);
    }
}
