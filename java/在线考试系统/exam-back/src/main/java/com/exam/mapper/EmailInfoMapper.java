package com.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.entity.EmailInfo;
import org.apache.ibatis.annotations.*;

import javax.validation.constraints.Email;

/**
 * (EmailInfo)表数据库访问层
 * @author LCH
 * @since 2022-04-12 16:18:17
 */
@Mapper
public interface EmailInfoMapper{
    @Insert("insert into email_info(id,title,toEmail) VALUES(null,#{title},#{toEmail})")
    public int addEmail(EmailInfo emailInfo);

    @Select("SELECT * FROM `email_info` where toEmail = #{v1} and title=#{v2}")
    public EmailInfo findEmailInfo(@Param("v1") String toEmail,@Param("v2") String code) ;

    @Delete("DELETE from email_info  where id  =#{v1}")
    public int delEmail(Integer id) ;
}

