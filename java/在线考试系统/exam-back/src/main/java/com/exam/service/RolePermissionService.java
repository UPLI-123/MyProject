package com.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.entity.RolePermission;
import io.swagger.models.auth.In;

/**
 * (RolePermission)表服务接口
 *
 * @author makejava
 * @since 2022-04-09 22:03:10
 */
public interface RolePermissionService  {

//     对角色权限表进行处理
    boolean dealRoleP(Integer roleId, Integer[] arry) ;

}

