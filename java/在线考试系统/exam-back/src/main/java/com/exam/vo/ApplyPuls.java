package com.exam.vo;

import lombok.Data;

/**
 * @projectName: exam
 * @package: com.exam.vo
 * @className: ApplyPuls
 * @author: Eric
 * @description: 对 申请信息 进行 封装
 * @date: 2022/4/29 10:49
 * @version: 1.0
 */
@Data
public class ApplyPuls {
    private Integer userId;

    private Integer roleId;

    private Integer power;

    private String stime;

    private Integer id;

    private String name;

    private String tel;

    private String email;

    private String password;
    //     添加一个code 方便传输值
    private String code;
    //   该用户当前的角色
    private String roleName;

}


