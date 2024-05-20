package com.exam.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (EaxmStore)表实体类
 *
 * @author makejava
 * @since 2022-04-14 10:08:13
 */
@SuppressWarnings("serial")
@Data
public class EaxmStore extends Model<EaxmStore> {
    //题库主键
    private Integer id;
    //创建者编号
    private Integer uid;
    //题库名称
    private String name;
    //题库题目数量
    private Integer count;
    //题库创建时间
    private String gtime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getGtime() {
        return gtime;
    }

    public void setGtime(String gtime) {
        this.gtime = gtime;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    }

