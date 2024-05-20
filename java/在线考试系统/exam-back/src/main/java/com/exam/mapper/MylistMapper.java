package com.exam.mapper;

import com.exam.entity.Mylist;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (Mylist)表数据库访问层
 * @author lch
 * @since 2022-05-09 15:51:19
 */
@Mapper
public interface MylistMapper {

    @Insert("insert into mylist (id,uid,date,btime,etime,content,power) VALUES(null,#{uid},#{date},#{btime},#{etime},#{content},#{power})")
    public int addMyList(Mylist mylist) ;

    @Select("SELECT * from mylist WHERE date = #{v1}")
    public List<Mylist> findByDate(String date) ;

    @Delete("DELETE from mylist where id =#{v1}")
    public int removeById(int id) ;

    @Update("UPDATE mylist set power =#{v2} WHERE id =#{v1} ")
    public int updateMylist(@Param("v1") int id, @Param("v2") int power) ;

}

