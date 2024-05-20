package com.exam.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (Apply)表实体类
 *
 * @author makejava
 * @since 2022-04-28 15:55:05
 */
@SuppressWarnings("serial")
@Data
public class Apply extends Model<Apply> {
    
    private Integer userId;
    
    private Integer roleId;
    
    private Integer power;

    private String stime ;

    private Integer id ;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.userId;
    }
    }

