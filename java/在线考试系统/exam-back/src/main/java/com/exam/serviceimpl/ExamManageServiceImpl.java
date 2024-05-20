package com.exam.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ExamManage;
import com.exam.entity.Message;
import com.exam.mapper.ExamManageMapper;
import com.exam.mapper.ExamProcessMapper;
import com.exam.mapper.MessageMapper;
import com.exam.service.ExamManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ExamManageServiceImpl implements ExamManageService {
    @Autowired
    private ExamManageMapper examManageMapper;

    @Autowired
    private ExamProcessMapper examProcessMapper ;

    @Autowired
    private MessageMapper messageMapper ;


    @Override
    public List<ExamManage> findAll(Integer id) {
        return examManageMapper.findAll2(id);
    }

    @Override
    public IPage<ExamManage> findAll(Page<ExamManage> page,Integer id) {
        return examManageMapper.findAll1(page,id);
    }

    @Override
    public ExamManage findById(Integer examCode) {
        return examManageMapper.findById(examCode);
    }

    @Override
    public int delete(Integer examCode) {
        return examManageMapper.delete(examCode);
    }

    @Override
    public int update(ExamManage exammanage) {
        return examManageMapper.update(exammanage);
    }

    @Override
    public int add(ExamManage exammanage) {
        if(exammanage.getTotalScore() == 100 ){ //  如果等于100 的话说明是过程化创建要改变过程化试卷的数量
//            首先修改 过程化考试的数量
            int l1 = examProcessMapper.updateCount(exammanage.getFatherid());
            int l2 = examManageMapper.add(exammanage) ;
            return l1&l2;
        }else{
            return examManageMapper.add(exammanage);
        }
    }

    @Override
    public ExamManage findOnlyPaperId() {
        return examManageMapper.findOnlyPaperId();
    }

    @Override
    public IPage<ExamManage> findAll(Page<ExamManage> page) {
        return examManageMapper.findAll(page,0);
    }

    @Override
    public int updateIsPublic(Integer id,Integer power) {
//        将当前试题状态转化为 待审核状态
        return examManageMapper.updatePublic(power,id);
    }

    @Override
    public IPage<ExamManage> findWSH(Page page) {
        return examManageMapper.findAll(page,3);
    }

    @Override
    @Transactional
    public int passExam(int createId, String content, int examCode, int tag) {
//        分为两步进行操作 ，首先向 消息 中添加信息
        Message message = new Message()  ;
        message.setUid(createId) ;
        message.setContent(content);
//         默认为 没有看过的信息
        message.setPower(0);
        message.setTime(new Date());
        int l1 = messageMapper.add(message);
//        进行修改 试卷 的状态
        if(tag == 1){ // 代表通过状态
            examManageMapper.updatePublic(0,examCode) ;
        }else if(tag == 2){ // 代表拒绝状态
            examManageMapper.updatePublic(2,examCode) ;
        }
        return 0;
    }
}
