package com.exam.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.EaxmStore;
import com.exam.entity.JudgeQuestion;
import com.exam.mapper.EaxmStoreMapper;
import com.exam.mapper.JudgeQuestionMapper;
import com.exam.service.JudgeQuestionService;
import com.exam.util.TimeConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class JudgeQuestionServiceImpl implements JudgeQuestionService {

    @Autowired
    private JudgeQuestionMapper judgeQuestionMapper;
    @Autowired
    private EaxmStoreMapper eaxmStoreMapper ;

    @Override
    public List<JudgeQuestion> findByIdAndType(Integer paperId) {
        return judgeQuestionMapper.findByIdAndType(paperId);
    }

    @Override
    public IPage<JudgeQuestion> findAll(Page<JudgeQuestion> page,Integer eid) {
        return judgeQuestionMapper.findAll(page,eid);
    }

    @Override
    public JudgeQuestion findOnlyQuestionId() {
        return judgeQuestionMapper.findOnlyQuestionId();
    }

    @Transactional
    @Override
    public int add(JudgeQuestion judgeQuestion) {
        int add = judgeQuestionMapper.add(judgeQuestion);
//        更改题库的数量和时间
        EaxmStore eaxmStore = eaxmStoreMapper.selectById(judgeQuestion.getEid());
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
        return judgeQuestionMapper.findBySubject(subject,pageNo);
    }

    @Override
    @Transactional
    public void removeJud(Integer id,Integer eid) {
//         首先删除
        judgeQuestionMapper.removeJud(id);
//        更新题库的数量
        EaxmStore eaxmStore = new EaxmStore() ;
        eaxmStore.setId(eid)  ;
        Date date = new Date() ;
        String time = TimeConvert.convert(date);
        eaxmStore.setGtime(time);
        eaxmStoreMapper.updateCount(eaxmStore) ;
    }

    @Override
    public void updateJud(JudgeQuestion judgeQuestion) {
        judgeQuestionMapper.updteJud(judgeQuestion);

    }
}
