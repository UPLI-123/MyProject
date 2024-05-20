package com.exam.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.entity.Message;
import com.exam.entity.UserRole;
import com.exam.mapper.ApplyMapper;
import com.exam.entity.Apply;
import com.exam.mapper.MessageMapper;
import com.exam.mapper.UserRoleMapper;
import com.exam.service.ApplyService;
import com.exam.util.TimeConvert;
import com.exam.vo.ApplyPuls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * (Apply)表服务实现类
 *
 * @author makejava
 * @since 2022-04-28 15:55:05
 */
@Service
public class ApplyServiceImpl implements ApplyService {

    @Autowired
    ApplyMapper applyMapper ;
    @Autowired
    UserRoleMapper userRoleMapper ;
    @Autowired
    MessageMapper messageMapper ;

    @Override
    public int addApply(Integer uid, Integer rid) {
//         新建一个 apply 对象 用来存储数据
        Apply apply = new Apply();
        apply.setUserId(uid) ;
        apply.setRoleId(rid) ;
        apply.setPower(0); // 0代表是处于申请状态，1 代表审核通过 2 代表审核未通过
        Date date = new Date() ;
        String time = TimeConvert.convert(date);
        apply.setStime(time);
        System.out.println(apply);
        return applyMapper.addApply(apply);
    }

    @Override
    public Apply findByUid(Integer uid) {
        return applyMapper.findInfo(uid,0);
    }

    @Override
    public Integer delApply(Integer id) {
        return applyMapper.delApply(id,0);
    }

    @Override
    public IPage<ApplyPuls> findAll(Page page) {
        return applyMapper.findAll(page);
    }

    @Override
    @Transactional
    public Integer submitCheck(Integer id, String content, Integer tag) {
//         提交 审核结果  ， 有两种情况 通过，或者 拒绝
        if(tag == 1){ // 通过 操作
            System.out.println(id) ;
//  通过 的 操作 ，首先修改 apply表为 power 为 1 ，然后向修改 userrole 表 ，然后在发送信息给申请者
            Integer a1 = applyMapper.updatePower(id, 1);
//            根据主键进行继续查询
            Apply apply = applyMapper.findById(id);
//            创建一个 Usererole 用来存储信息
            UserRole userRole = new UserRole()    ;
            userRole.setRoleId(apply.getRoleId()) ;
            userRole.setUserId(apply.getUserId()) ;
//            进行新增操作
            userRoleMapper.addUserRole(userRole.getRoleId(),userRole.getUserId()) ;
//            进行消息的新增
            Message message = new Message() ;
            message.setPower(0) ; // 设置为未读
            message.setContent(content) ;
            message.setUid(apply.getUserId());
            Date date  = new Date() ;
            message.setTime(date);
            int a2 = messageMapper.add(message);
            return a1&a2;
        }else if(tag == 2){ //  拒绝 操作

//  拒绝 的 操作 ，首先修改 apply表为 power 为 2  ，然后在发送信息给申请者
            Integer a1 = applyMapper.updatePower(id,2);
            Message message = new Message() ;
            message.setPower(0) ; // 设置为未读
            message.setContent(content) ;
            Apply apply = applyMapper.findById(id);
            message.setUid(apply.getUserId());
            Date date  = new Date() ;
            message.setTime(date);
            int a2 = messageMapper.add(message);
            return a1&a2;
        }else {
            return 0;
        }

    }
}

