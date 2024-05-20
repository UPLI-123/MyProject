package com.exam.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.entity.ExamManage;
import com.exam.mapper.ExamManageMapper;
import com.exam.mapper.ExamProcessMapper;
import com.exam.entity.ExamProcess;
import com.exam.service.ExamProcessService;
import com.sun.mail.imap.protocol.ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (ExamProcess)表服务实现类
 *
 * @author makejava
 * @since 2022-05-04 16:02:37
 */
@Service
public class ExamProcessServiceImpl implements ExamProcessService {

    @Autowired
    ExamProcessMapper examProcessMapper ;
    @Autowired
    ExamManageMapper examManageMapper ;

    @Override
    public int addExamP(ExamProcess process) {
        return examProcessMapper.add(process);
    }

    @Override
    public IPage findByUid(Page page, Integer id) {
        return examProcessMapper.findByUid(page,id);
    }

    @Override
    public ExamProcess findById(int id) {
        return examProcessMapper.findById(id);
    }

    @Override
    public int updateExamP(ExamProcess examProcess) {
        return examProcessMapper.updateInfo(examProcess);
    }

    @Override
    public List<ExamManage> findByFid(Integer fid) {
        return examManageMapper.findByFid(fid);
    }
}

