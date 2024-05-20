package com.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * (Permission)表服务接口
 *
 * @author makejava
 * @since 2022-04-09 22:03:09
 */
public interface PermissionService  {

    List<Map<String,Object>> findTree() ;

    Map<String,Object> initTree(Integer id) ;

}

