package com.exam.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (ExamProcess)表实体类
 *
 * @author makejava
 * @since 2022-05-04 16:02:36
 */
@SuppressWarnings("serial")
@Data
public class ExamProcess extends Model<ExamProcess> {
    //过程化考试的id
    private Integer id;
    //过程化考试的姓名
    private String ename;
    //过程化考试的简介
    private String brief;
    //包含的试题数量
    private Integer count;
    // 创建人id
    private Integer uid ;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

