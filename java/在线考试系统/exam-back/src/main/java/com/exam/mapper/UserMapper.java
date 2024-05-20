package com.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * (User)表数据库访问层
 *
 * @author lch
 * @since 2022-04-09 19:33:15
 */
@Mapper
public interface UserMapper  {

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into user(id,name,tel,email,password) VALUES(null,#{name},#{tel},#{email},#{password})")
    public int addUser(User user) ;

//     查询用户的信息
    @Select("select * from user where email =#{v} or tel = #{v}")
    public User selectByUsername(String username) ;

    @Update("update user set password = #{password} where email = #{email} ")
    public int updatePwd(User user) ;

    @Select("select u.*,r.role_name,r.role_id from user u , user_role ur , role r \n" +
            "where u.id = ur.user_id and r.role_id = ur.role_id \n")
    public IPage<User> selectAll(Page page) ;

    @Delete("DELETE from user where id = #{v1}")
    public void delUserById(Integer id) ;
}

