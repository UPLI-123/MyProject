package com.exam.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (EmailInfo)表实体类
 *
 * @author makejava
 * @since 2022-04-12 16:18:17
 */
@SuppressWarnings("serial")
@Data
public class EmailInfo extends Model<EmailInfo> {
    //邮件编号
    private Integer id;
    //邮件内容
    private String toEmail;
    //发送邮箱
    private String title ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

