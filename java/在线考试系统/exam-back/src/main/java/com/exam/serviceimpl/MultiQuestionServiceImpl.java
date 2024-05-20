package com.exam.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.EaxmStore;
import com.exam.entity.MultiQuestion;
import com.exam.mapper.EaxmStoreMapper;
import com.exam.mapper.MultiQuestionMapper;
import com.exam.service.MultiQuestionService;
import com.exam.util.TimeConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MultiQuestionServiceImpl implements MultiQuestionService {

    @Autowired
    private MultiQuestionMapper multiQuestionMapper;

    @Autowired
    private EaxmStoreMapper eaxmStoreMapper ;

    @Override
    public List<MultiQuestion> findByIdAndType(Integer PaperId) {
        return multiQuestionMapper.findByIdAndType(PaperId);
    }

    @Override
    public IPage<MultiQuestion> findAll(Page<MultiQuestion> page,Integer eid) {
        return multiQuestionMapper.findAll(page,eid);
    }

    @Override
    public MultiQuestion findOnlyQuestionId() {
        return multiQuestionMapper.findOnlyQuestionId();
    }

    @Transactional
    @Override
    public int add(MultiQuestion multiQuestion) {
        int add = multiQuestionMapper.add(multiQuestion);
        EaxmStore eaxmStore = eaxmStoreMapper.selectById(multiQuestion.getEid());
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
        return multiQuestionMapper.findBySubject(subject,pageNo);
    }

    @Override
    @Transactional
    public Integer removeMul(Integer id,Integer eid) {
        // 删除 当前题
        Integer l = multiQuestionMapper.removeMul(id);
        // 修改当前题的数目
        EaxmStore eaxmStore = new EaxmStore() ;
        eaxmStore.setId(eid);
        Date date = new Date() ;
        String time = TimeConvert.convert(date);
        eaxmStore.setGtime(time);
        Integer l1 = eaxmStoreMapper.updateCount(eaxmStore);
        return  l1&l ;
    }

    @Override
    public void updateMul(MultiQuestion multiQuestion) {
        multiQuestionMapper.updateMul(multiQuestion);
    }


}
