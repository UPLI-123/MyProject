package com.exam.serviceimpl;

import com.exam.entity.Mylist;
import com.exam.mapper.MylistMapper;
import com.exam.service.MylistService;
import com.exam.util.TimeConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * (Mylist)表服务实现类
 *
 * @author makejava
 * @since 2022-05-09 15:51:20
 */
@Service
public class MylistServiceImpl  implements MylistService {

    @Autowired
    MylistMapper mylistMapper ;


    @Override
    public List<Mylist> findAllList(String time) {
        return mylistMapper.findByDate(time);
    }


    @Override
    public int addList(Mylist mylist) {
//        新增的 事务默认是未完成的
        mylist.setPower(0);
        Date time = new Date() ;
        String date = TimeConvert.convert(time);
        mylist.setDate(date);
        return mylistMapper.addMyList(mylist);
    }


    @Override
    public int updateListPower(int id,int power) {
        return mylistMapper.updateMylist(id,power);
    }


    @Override
    public int delById(int id) {
        return mylistMapper.removeById(id) ;
    }
}

