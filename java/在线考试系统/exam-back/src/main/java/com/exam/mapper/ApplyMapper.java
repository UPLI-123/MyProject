package com.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.Apply;
import com.exam.vo.ApplyPuls;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;
import org.omg.CORBA.INTERNAL;

import javax.annotation.security.PermitAll;

/**
 * (Apply)表数据库访问层
 *
 * @author makejava
 * @since 2022-04-28 15:55:05
 */
@Mapper
public interface ApplyMapper{

    @Insert("insert into apply(id,user_id,role_id,power,stime) VALUES(null,#{userId},#{roleId},#{power},#{stime})")
    public Integer addApply(Apply apply) ;

    @Select("SELECT * from apply WHERE user_id = #{v1} and  power = #{v2}")
    public Apply findInfo(@Param("v1") Integer uid, @Param("v2") Integer power) ;

    @Delete("DELETE from apply where user_id = #{v1} and power  = #{v2} ")
    public Integer delApply(@Param("v1") Integer id, @Param("v2") Integer power) ;

    @Select("SELECT a.* ,role_name,name from apply a , user u , role r \n" +
            "where a.user_id = u.id and a.role_id = r.role_id")
    public IPage<ApplyPuls> findAll(Page page) ;

    @Update("UPDATE apply set power = #{v1} where id = #{v2} ")
    public Integer updatePower(@Param("v2") Integer id, @Param("v1") Integer id2) ;

    @Select("SELECT * from apply where id  = #{v1}")
    public Apply findById(Integer id) ;
}

