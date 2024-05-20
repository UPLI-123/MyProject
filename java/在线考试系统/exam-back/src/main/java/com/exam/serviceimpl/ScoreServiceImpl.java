package com.exam.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ExamManage;
import com.exam.entity.Message;
import com.exam.entity.Score;
import com.exam.mapper.ExamManageMapper;
import com.exam.mapper.MessageMapper;
import com.exam.mapper.ScoreMapper;
import com.exam.service.ScoreService;
import com.exam.util.TimeConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.soap.SOAPConnection;
import java.util.Date;
import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private ExamManageMapper examManageMapper ;
    @Autowired
    private MessageMapper messageMapper ;

    @Override
    @Transactional
    public int add(Score score) {
        // 首先进行一下判断 ，对score 的先进行一下查找 ，如果 三项都相同的话，做修改操作，就不做 新增操作，否则做新增操作
        List<Score> byId = scoreMapper.findById(score.getStudentId());
        boolean byScoreId = false;
        for(Score s: byId){
            System.out.println(s.getStudentId());
            System.out.println(score.getStudentId());
            System.out.println(s.getExamCode());
            System.out.println(score.getExamCode());
//             一个java 的小坑
            if((s.getStudentId().equals(score.getStudentId()))&& (s.getExamCode().equals(score.getExamCode()))){
                byScoreId = true;
                System.out.println(byScoreId);
                score.setScoreId(s.getScoreId());
                break;
            }
        }
        int add = 0 ;
        // 将考试状态设置为 未参见考试

        if(byScoreId){
            // 进行更新操作
            // 更新操作 又分为以下几种
            if(score.getScore()!=null){
//                3 状态 代表考生 已经完成了考试
                score.setState(3);
                add = scoreMapper.updateSocre(score);

            }else {
                score.setState(1);
                add = scoreMapper.updateSocre(score);
            }
        }else{
            score.setState(1);
            add = scoreMapper.add(score);
        }
        return add;
    }

    @Override
    public List<Score> findAll() {
        return scoreMapper.findAll();
    }

    @Override
    public IPage<Score> findById(Page page, Integer studentId) {
        return scoreMapper.findById(page, studentId);
    }

    @Override
    public List<Score> findById(Integer studentId) {
        return scoreMapper.findById(studentId);
    }

    @Override
    public List<Score> findByExamCode(Integer examCode) {
        return scoreMapper.findByExamCode(examCode);
    }

    @Override
    @Transactional
    public int pass(Integer id,Integer uid,Integer examCode) {
//         通过的话主要分为两个部分 ，一个部分是   更改 score表的状态 ，一个是 向消息表中天剑数据
        int r1 = scoreMapper.updateSocreState(0, id);
//        向消息表中添加数据
        Message message = new Message() ;
        message.setUid(uid) ;
        ExamManage exam = examManageMapper.findById(examCode);
//        组合成为 要传递的信息
        String content  = "你的申请已经被通过，请你在"+exam.getExamDate() +"参加"+exam.getSource() +"考试"
                +"同时注意："+exam.getTips() ;
        message.setContent(content);
        message.setPower(0); // 0代表未读状态，1代表已经读的状态
        Date date = new Date() ;
        message.setTime(date);
        int r2 = messageMapper.add(message);
        return r1&r2 ;
    }

    @Override
    public int checkPower(Integer uid, Integer examCode) {
        Score score = scoreMapper.selectByPower(uid, examCode, 0);
//        System.out.println(score) ;
//         1代表报名了 ，0代表没有报名 ，2 代表已经参加考试
        if(score!=null && !score.equals("") ){
            if(score.getScore()!=null){
                return 2;
            }
            return 1;
        }
        return 0;
    }
    @Override
    public String findImg(Integer uid, Integer examCode) {
//         实现 从数据库中查找出来相应的文件的路径
        Score score = scoreMapper.selectByPower(uid, examCode, 0);
        return score.getImage();
    }

//     获得 当前用户 所有的考试信息
    @Override
    public List<ExamManage> getAllByUid(int id) {
        return examManageMapper.getAllByuid(id);
    }

    @Override
    public List<ExamManage> findByTime(int id, String time) {
        return examManageMapper.getAllByTime(id,time);
    }
}
