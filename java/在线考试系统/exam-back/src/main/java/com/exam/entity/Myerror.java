package com.exam.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (Myerror)表实体类
 *
 * @author makejava
 * @since 2022-05-10 14:54:32
 */
@SuppressWarnings("serial")
@Data
public class Myerror extends Model<Myerror> {
    //主键
    private Integer id;
    //代表是那种题型：1 代表 选择 2代表判断 3代表填空
    private Integer type;
    //题目编号
    private Integer paperId;
    //考试编号
    private Integer examCode;
    //用户编号
    private Integer uid;

    private Integer questionId ;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPaperid() {
        return paperId;
    }

    public void setPaperid(Integer paperid) {
        this.paperId = paperid;
    }

    public Integer getExamcode() {
        return examCode;
    }

    public void setExamcode(Integer examcode) {
        this.examCode = examcode;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

