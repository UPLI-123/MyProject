package com.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.entity.User;
import com.exam.entity.UserRole;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

/**
 * (UserRole)表数据库访问层
 *
 * @author lch
 * @since 2022-04-09 22:03:08
 */
@Mapper
public interface UserRoleMapper{

    /**
     * @param userID:  用户id
      * @return int
     * @author lch
     * @description 将用户和角色信息加入到表中去，默认一开始注册的都是普通用户
     * @date  2022年4月10日09:10:26
     */
    @Insert("insert into user_role(user_role.role_id,user_role.user_id) values(#{v2},#{v1})")
    public int addUserRole(@Param("v2") Integer rID,@Param("v1") Integer userID) ;

    @Update("update user_role set role_id = #{v1} where user_id = #{v2}")
    public int updateUserRole(@Param("v1")Integer roleId ,@Param("v2") Integer userId) ;

    @Delete("DELETE from user_role where user_id = #{v1}")
    public void delByUid(Integer id) ;

    @Delete("DELETE from user_role where role_id = #{v1}")
    public void delByRid(Integer id) ;

    @Select("SELECT u.* from user u , user_role ur\n" +
            "where u.id = ur.user_id and ur.role_id =#{v1}")
    public List<User> findUsers(Integer rid) ;


    @Select("SELECT *from user_role where user_role.user_id = #{v1}")
    public UserRole findByUid(Integer id) ;


}

