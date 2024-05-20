package com.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.MBlog;
import org.apache.ibatis.annotations.*;

import javax.validation.constraints.Max;

/**
 * (MBlog)表数据库访问层
 *
 * @author makejava
 * @since 2022-05-12 11:14:48
 */
@Mapper
public interface MBlogMapper {

//   根据考试编号和 状态进行查询

//    增加操作
    @Insert("INSERT INTO m_blog(user_id,title,description,content,created,status,examCode)" +
            "VALUES(#{userId},#{title},#{description},#{content},#{created},#{status},#{examCode})")
    int addBlog(MBlog mBlog) ;

//    根据主键进行删除
    @Delete("DELETE from m_blog WHERE id =#{v1}")
    int removeBlog(int id) ;

//    根据考试编号 和 状态进行查询
    @Select("SELECT * from m_blog where examCode = #{examCode} and status = #{status}")
    IPage<MBlog> findBlogsByExamcode(@Param("page") Page page,@Param("examCode") int examCode,@Param("status") int status) ;
//    根据用户名和状态进行查询
    @Select("SELECT * from m_blog WHERE user_id = #{uid}  and STATUS = #{status}")
    IPage<MBlog> findBlogsByUid(@Param("page") Page page, @Param("uid") int uid,@Param("status") int status) ;

//    根据id 进行修改 该条记录的  状态
    @Update("update m_blog set status = #{status} WHERE id = #{id}")
    int updateBlog(@Param("status") int status,@Param("id") int id) ;

    @Update("UPDATE m_blog set title = #{title} , description =#{description} ,content = #{content} and created = #{content}" +
            " WHERE id =#{id}")
    int updateById(MBlog mBlog) ;

//     根据主键进行查询
    @Select("SELECT * from m_blog where id =#{v1}")
    MBlog findById(int  id);



}

