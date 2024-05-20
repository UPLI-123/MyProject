package com.exam.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.entity.FillQuestion;
import com.exam.entity.JudgeQuestion;
import com.exam.entity.MultiQuestion;
import com.exam.mapper.MyerrorMapper;
import com.exam.entity.Myerror;
import com.exam.service.MyerrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Myerror)表服务实现类
 *
 * @author makejava
 * @since 2022-05-10 14:54:32
 */
@Service
public class MyerrorServiceImpl  implements MyerrorService {

    @Autowired
    private MyerrorMapper myerrorMapper ;

    @Override
    public List<MultiQuestion> findErrorMul(int uid,int paperId) {
        return myerrorMapper.findErrorMul(uid,paperId);
    }

    @Override
    public List<JudgeQuestion> findErrorJud(int uid,int paperId) {
        return myerrorMapper.findErrorJud(uid,paperId);
    }

    @Override
    public List<FillQuestion> findErrorFill(int uid,int paperId) {
        return myerrorMapper.findErrorFill(uid,paperId);
    }

    @Override
    public int addMyError(Myerror myerror) {
        return myerrorMapper.addMyError(myerror);
    }

    @Override
    public int removeMyError(int id) {
        return myerrorMapper.removeMyError(id);
    }
}

