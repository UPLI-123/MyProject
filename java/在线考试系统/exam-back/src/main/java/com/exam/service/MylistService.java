package com.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.entity.Mylist;

import java.util.List;

/**
 * (Mylist)表服务接口
 *
 * @author makejava
 * @since 2022-05-09 15:51:20
 */
public interface MylistService  {

    //  查询当前 所有的事务
    List<Mylist> findAllList(String time) ;

    // 增加 事务列表
    int addList(Mylist mylist) ;

    // 修改状态
    int updateListPower(int id,int power) ;

    // 删除事务
    int delById(int id) ;

}

